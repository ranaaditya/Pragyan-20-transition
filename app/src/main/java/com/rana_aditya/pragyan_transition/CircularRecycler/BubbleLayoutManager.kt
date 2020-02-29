import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class BubbleLayoutManager (private val itemSize: Int) : RecyclerView.LayoutManager() {

    private val children = mutableListOf<Child>()

    override fun generateDefaultLayoutParams() = RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        fillChildren()
        detachAndScrapAttachedViews(recycler)
        fillView(recycler)
    }

    override fun canScrollVertically() = true
    override fun canScrollHorizontally() = true

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        val delta = scrollVerticallyInternal(dy)
        offsetChildren(yOffset = -delta)
        offsetChildrenVertical(-delta)
        fillAndRecycle(recycler)
        return dy
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        val delta = scrollHorizontallyInternal(dx)
        offsetChildren(xOffset = -delta)
        offsetChildrenHorizontal(-delta)
        fillAndRecycle(recycler)
        return dx
    }

    private fun fillAndRecycle(recycler: RecyclerView.Recycler) {
        val itemCount = itemCount
        for (i in 0 until itemCount) {
            if (i < children.size) {
                val child = children[i]
                val childRect = childRect(child)
                val alreadyDrawn = alreadyDrawn(child)
                if (!alreadyDrawn && fitOnScreen(childRect)) {
                    val view = recycler.getViewForPosition(i)
                    addView(view)
                    measureChildWithMargins(view, 0, 0)
                    layoutDecorated(view, childRect.left, childRect.top, childRect.right, childRect.bottom)
                }
            }
        }
        recycleViews(recycler)
        updateScales()
    }

    private fun recycleViews(recycler: RecyclerView.Recycler) {
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view != null && !fitOnScreen(view)) {
                detachView(view)
                recycler.recycleView(view)
            }
        }
    }

    private fun fillView(recycler: RecyclerView.Recycler) {
        val itemCount = itemCount
        for (i in 0 until itemCount) {
            if (i < children.size) {
                val childRect = childRect(children[i])
                if (fitOnScreen(childRect)) {
                    val view = recycler.getViewForPosition(i)
                    addView(view)
                    measureChildWithMargins(view, 0, 0)
                    layoutDecorated(view, childRect.left, childRect.top, childRect.right, childRect.bottom)
                }
            }
        }
        updateScales()
    }

    private fun scrollVerticallyInternal(dy: Int): Int {
        if (childCount == 0) {
            return 0
        }

        val highestChild = children.minBy { it.y }
        val lowestChild = children.maxBy { it.y }

        if (highestChild != null && lowestChild != null) {
            if (lowestChild.y + itemSize / 2 <= height && highestChild.y - itemSize / 2 >= 0) {
                return 0
            }
        } else {
            return 0
        }

        var delta = 0
        if (dy < 0) {
            delta = if (highestChild.y - itemSize / 2 < 0) {
                max(highestChild.y - itemSize / 2, dy)
            } else 0
        } else if (dy > 0) {
            delta = if (lowestChild.y + itemSize / 2 > height) {
                min(lowestChild.y + itemSize / 2 - height, dy)
            } else 0
        }
        return delta
    }

    private fun scrollHorizontallyInternal(dx: Int): Int {
        if (childCount == 0) {
            return 0
        }

        val mostLeftChild = children.minBy { it.x }
        val mostRightChild = children.maxBy { it.x }

        if (mostLeftChild != null && mostRightChild != null) {
            if (mostLeftChild.x - itemSize / 2 >= 0 && mostRightChild.x + itemSize / 2 <= width) {
                return 0
            }
        } else {
            return 0
        }

        var delta = 0
        if (dx < 0) {
            delta = if (mostLeftChild.x - itemSize / 2 < 0) {
                max(mostLeftChild.x - itemSize / 2, dx)
            } else 0
        } else if (dx > 0) {
            delta = if (mostRightChild.x + itemSize / 2 > width) {
                min(mostRightChild.x + itemSize / 2 - width, dx)
            } else 0
        }
        return delta
    }

    private fun offsetChildren(xOffset: Int = 0, yOffset: Int = 0) {
        children.forEach { it.offset(xOffset, yOffset) }
    }

    private fun updateScales() {
        val centerX = width / 2
        val centerY = height / 2
        val distanceMap = sortedMapOf<Int, MutableList<Int>>()
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view != null) {
                val distance = distance(centerX, centerY, view.x.toInt() + view.width / 2, view.y.toInt() + view.height / 2)
                val positions = distanceMap.getOrPut(distance) { mutableListOf() }
                positions.add(i)
            }
        }
        var scale = 1f
        distanceMap.keys.forEach { key ->
            val positions = distanceMap[key]
            if (positions != null) {
                for (position in positions) {
                    val view = getChildAt(position)
                    if (view != null) {
                        view.scaleX = scale
                        view.scaleY = scale
                    }
                }
            }
            scale *= 0.95f
        }
    }

    private fun distance(x1: Int, y1: Int, x2: Int, y2: Int) = sqrt(((x2 - x1) * (x2 - x1)).toFloat() + ((y2 - y1) * (y2 - y1)).toFloat()).toInt()

    private fun childRect(child: Child): Rect {
        val left = child.x - itemSize / 2
        val top = child.y - itemSize / 2
        val right = left + itemSize
        val bottom = top + itemSize
        return Rect(left, top, right, bottom)
    }

    private fun fillChildren() {
        children.clear()
        val centerX = width / 2
        val centerY = height / 2

        val itemCount = itemCount
        if (itemCount > 0) {
            children.add(Child(centerX, centerY))
            if (itemCount > 1) {
                for (i in 1 until itemCount) {
                    fillChildrenRelative(children[i - 1], itemCount)
                }
            }
        }
    }

    private fun fillChildrenRelative(anchorChild: Child, itemCount: Int) {
        var i = 0
        var direction = Direction.initial()
        while (i < 4 && children.size < itemCount) {
            val childX = anchorChild.x + (itemSize / 2) * direction.widthMultiplier
            val childY = anchorChild.y + itemSize * direction.heightMultiplier
            if (!hasChild(childX, childY)) {
                children.add(Child(childX, childY))
            }
            direction = direction.next()
            i++
        }
    }

    private fun hasChild(x: Int, y: Int) = children.any { it.x == x && it.y == y }

    private fun fitOnScreen(view: View) = fitOnScreen(getViewRect(view))

    private fun getViewRect(view: View) = Rect(
            getDecoratedLeft(view),
            getDecoratedTop(view),
            getDecoratedRight(view),
            getDecoratedBottom(view)
    )

    private fun fitOnScreen(rect: Rect): Boolean = rect.intersects(0, 0, width, height)

    private fun alreadyDrawn(child: Child): Boolean {
        val rect = childRect(child)
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view != null) {
                val viewRect = getViewRect(view)
                if (viewRect.intersects(rect.left, rect.top, rect.right, rect.bottom)) {
                    return true
                }
            }
        }
        return false
    }

    private data class Child(
            var x: Int,
            var y: Int
    ) {

        fun offset(xOffset: Int = 0, yOffset: Int = 0) {
            x += xOffset
            y += yOffset
        }
    }
}


// Direction.kt
internal sealed class Direction(
        val widthMultiplier: Int, val heightMultiplier: Int
) {
    companion object {
        internal fun initial(): Direction = LeftTop
    }
}

internal object LeftTop : Direction(-1, -1)
internal object RightTop : Direction(1, -1)
internal object LeftBottom : Direction(-1, 1)
internal object RightBottom : Direction(1, 1)

internal fun Direction.next() = when (this) {
    is LeftTop -> RightTop
    is RightTop -> LeftBottom
    is LeftBottom -> RightBottom
    is RightBottom -> LeftTop
}