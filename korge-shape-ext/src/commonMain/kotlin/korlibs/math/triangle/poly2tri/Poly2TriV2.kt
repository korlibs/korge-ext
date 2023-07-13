package korlibs.math.triangle.poly2tri

/*
object Poly2TriV2 {
    class Node(
        var point: Point,
        var triangle: Triangle? = null,
        var prev: Node? = null,
        var next: Node? = null,
        var value: Double = 0.0
    )
    //class Node(var point: Point, var triangle: Triangle) {
    //    var next: Node? = null
    //    var prev: Node? = null
    //}


    data class Point(
        var x: Double = 0.0,
        var y: Double = 0.0,
        var edgeList: MutableList<Edge> = mutableListOf()
    ) {
        init {
            x = 0.0
            y = 0.0
        }

        fun set_zero() {
            x = 0.0
            y = 0.0
        }

        fun set(x_: Double, y_: Double) {
            x = x_
            y = y_
        }

        operator fun unaryMinus(): Point {
            val v = Point(-x, -y)
            return v
        }

        operator fun plusAssign(v: Point) {
            x += v.x
            y += v.y
        }

        operator fun minusAssign(v: Point) {
            x -= v.x
            y -= v.y
        }

        operator fun timesAssign(a: Double) {
            x *= a
            y *= a
        }

        fun length(): Double {
            return kotlin.math.sqrt(x * x + y * y)
        }

        fun normalize(): Double {
            val len = length()
            x /= len
            y /= len
            return len
        }
    }

    data class Edge(var p: Point, var q: Point)

    class Triangle(a: Point, b: Point, c: Point) {
        var constrainedEdge = BooleanArray(3)
        var delaunayEdge = BooleanArray(3)
        var interior = false
        private val points_ = arrayOf<Point?>(a, b, c)
        private val neighbors_ = arrayOfNulls<Triangle>(3)

        fun getPoint(index: Int): Point {
            return points_[index]!!
        }

        fun getNeighbor(index: Int): Triangle? {
            return neighbors_[index]
        }

        fun contains(p: Point): Boolean {
            return p == points_[0] || p == points_[1] || p == points_[2]
        }

        fun contains(e: Edge): Boolean {
            return contains(e.p) && contains(e.q)
        }

        fun contains(p: Point, q: Point): Boolean {
            return contains(p) && contains(q)
        }

        fun edgeIndex(p1: Point, p2: Point): Int {
            if (points_[0] == p1) {
                if (points_[1] == p2) {
                    return 2
                } else if (points_[2] == p2) {
                    return 1
                }
            } else if (points_[1] == p1) {
                if (points_[2] == p2) {
                    return 0
                } else if (points_[0] == p2) {
                    return 2
                }
            } else if (points_[2] == p1) {
                if (points_[0] == p2) {
                    return 1
                } else if (points_[1] == p2) {
                    return 0
                }
            }
            return -1
        }

        fun markConstrainedEdge(index: Int) {
            constrainedEdge[index] = true
        }

        fun markConstrainedEdge(edge: Edge) {
            markConstrainedEdge(edge.p, edge.q)
        }

        // Mark edge as constrained
        fun markConstrainedEdge(p: Point, q: Point) {
            when {
                q == points_[0] && p == points_[1] || q == points_[1] && p == points_[0] -> constrainedEdge[2] = true
                q == points_[0] && p == points_[2] || q == points_[2] && p == points_[0] -> constrainedEdge[1] = true
                q == points_[1] && p == points_[2] || q == points_[2] && p == points_[1] -> constrainedEdge[0] = true
            }
        }


        fun markNeighbor(p1: Point, p2: Point, t: Triangle) {
            if ((p1 == points_[2] && p2 == points_[1]) || (p1 == points_[1] && p2 == points_[2])) {
                neighbors_[0] = t
            } else if ((p1 == points_[0] && p2 == points_[2]) || (p1 == points_[2] && p2 == points_[0])) {
                neighbors_[1] = t
            } else if ((p1 == points_[0] && p2 == points_[1]) || (p1 == points_[1] && p2 == points_[0])) {
                neighbors_[2] = t
            } else {
                error("Invalid points")
            }
        }

        fun markNeighbor(t: Triangle) {
            if (t.contains(points_[1]!!, points_[2]!!)) {
                neighbors_[0] = t
                t.markNeighbor(points_[1]!!, points_[2]!!, this)
            } else if (t.contains(points_[0]!!, points_[2]!!)) {
                neighbors_[1] = t
                t.markNeighbor(points_[0]!!, points_[2]!!, this)
            } else if (t.contains(points_[0]!!, points_[1]!!)) {
                neighbors_[2] = t
                t.markNeighbor(points_[0]!!, points_[1]!!, this)
            }
        }

        fun clear() {
            for (neighbor in neighbors_) {
                neighbor?.clearNeighbor(this)
            }
            clearNeighbors()
            points_[0] = null
            points_[1] = null
            points_[2] = null
        }

        fun clearNeighbor(triangle: Triangle?) {
            if (neighbors_[0] == triangle) {
                neighbors_[0] = null
            } else if (neighbors_[1] == triangle) {
                neighbors_[1] = null
            } else {
                neighbors_[2] = null
            }
        }

        fun clearNeighbors() {
            neighbors_[0] = null
            neighbors_[1] = null
            neighbors_[2] = null
        }

        fun clearDelunayEdges() {
            delaunayEdge[0] = false
            delaunayEdge[1] = false
            delaunayEdge[2] = false
        }

        fun pointCW(point: Point): Point? {
            return when {
                point == points_[0] -> points_[2]
                point == points_[1] -> points_[0]
                point == points_[2] -> points_[1]
                else -> null
            }
        }

        fun pointCCW(point: Point): Point? {
            return when {
                point == points_[0] -> points_[1]
                point == points_[1] -> points_[2]
                point == points_[2] -> points_[0]
                else -> null
            }
        }

        fun neighborAcross(point: Point): Triangle? {
            return when {
                point == points_[0] -> neighbors_[0]
                point == points_[1] -> neighbors_[1]
                else -> neighbors_[2]
            }
        }

        fun neighborCW(point: Point): Triangle? {
            return when {
                point == points_[0] -> neighbors_[1]
                point == points_[1] -> neighbors_[2]
                else -> neighbors_[0]
            }
        }

        fun neighborCCW(point: Point): Triangle? {
            return when {
                point == points_[0] -> neighbors_[2]
                point == points_[1] -> neighbors_[0]
                else -> neighbors_[1]
            }
        }

        fun getConstrainedEdgeCCW(p: Point): Boolean {
            return when {
                p == points_[0] -> constrainedEdge[2]
                p == points_[1] -> constrainedEdge[0]
                else -> constrainedEdge[1]
            }
        }

        fun getConstrainedEdgeCW(p: Point): Boolean {
            return when {
                p == points_[0] -> constrainedEdge[1]
                p == points_[1] -> constrainedEdge[2]
                else -> constrainedEdge[0]
            }
        }

        fun setConstrainedEdgeCCW(p: Point, ce: Boolean) {
            when {
                p == points_[0] -> constrainedEdge[2] = ce
                p == points_[1] -> constrainedEdge[0] = ce
                else -> constrainedEdge[1] = ce
            }
        }

        fun setConstrainedEdgeCW(p: Point, ce: Boolean) {
            when {
                p == points_[0] -> constrainedEdge[1] = ce
                p == points_[1] -> constrainedEdge[2] = ce
                else -> constrainedEdge[0] = ce
            }
        }

        fun getDelunayEdgeCCW(p: Point): Boolean {
            return when {
                p == points_[0] -> delaunayEdge[2]
                p == points_[1] -> delaunayEdge[0]
                else -> delaunayEdge[1]
            }
        }

        fun getDelunayEdgeCW(p: Point): Boolean {
            return when {
                p == points_[0] -> delaunayEdge[1]
                p == points_[1] -> delaunayEdge[2]
                else -> delaunayEdge[0]
            }
        }

        fun setDelunayEdgeCCW(p: Point, e: Boolean) {
            when {
                p == points_[0] -> delaunayEdge[2] = e
                p == points_[1] -> delaunayEdge[0] = e
                else -> delaunayEdge[1] = e
            }
        }

        fun setDelunayEdgeCW(p: Point, e: Boolean) {
            when {
                p == points_[0] -> delaunayEdge[1] = e
                p == points_[1] -> delaunayEdge[2] = e
                else -> delaunayEdge[0] = e
            }
        }

        fun circumscribeContains(point: Point): Boolean {
            check(isCounterClockwise())
            val dx = points_[0]!!.x - point.x
            val dy = points_[0]!!.y - point.y
            val ex = points_[1]!!.x - point.x
            val ey = points_[1]!!.y - point.y
            val fx = points_[2]!!.x - point.x
            val fy = points_[2]!!.y - point.y

            val ap = dx * dx + dy * dy
            val bp = ex * ex + ey * ey
            val cp = fx * fx + fy * fy

            return (dx * (fy * bp - cp * ey) - dy * (fx * bp - cp * ex) + ap * (fx * ey - fy * ex)) < 0
        }

        fun isCounterClockwise(): Boolean {
            return (points_[1]!!.x - points_[0]!!.x) * (points_[2]!!.y - points_[0]!!.y) -
                (points_[2]!!.x - points_[0]!!.x) * (points_[1]!!.y - points_[0]!!.y) > 0
        }

        fun isInterior(): Boolean {
            return interior
        }

        fun setInterior(b: Boolean) {
            interior = b
        }
    }

    fun createTriangle(a: Point, b: Point, c: Point): Triangle {
        return Triangle(a, b, c)
    }

    fun isDelaunay(triangles: List<Triangle>): Boolean {
        for (triangle in triangles) {
            for (other in triangles) {
                if (triangle == other) continue
                for (i in 0 until 3) {
                    if (triangle.circumscribeContains(other.getPoint(i))) {
                        return false
                    }
                }
            }
        }
        return true
    }

    class AdvancingFront(var head: Node, var tail: Node) {
        private var searchNode: Node = head

        fun locateNode(x: Double): Node? {
            var node = searchNode

            if (x < node.value) {
                while (node.prev != null) {
                    node = node.prev!!
                    if (x >= node.value) {
                        searchNode = node
                        return node
                    }
                }
            } else {
                while (node.next != null) {
                    node = node.next!!
                    if (x < node.value) {
                        searchNode = node.prev!!
                        return node.prev
                    }
                }
            }
            return null
        }

        fun findSearchNode(x: Double): Node {
            // TODO: implement BST index
            return searchNode
        }

        fun locatePoint(point: Point): Node? {
            val px = point.x
            var node: Node? = findSearchNode(px)
            val nx = node!!.point.x

            if (px == nx) {
                if (point != node.point) {
                    // We might have two nodes with same x value for a short time
                    if (point == node.prev?.point) {
                        node = node.prev
                    } else if (point == node.next?.point) {
                        node = node.next
                    } else {
                        throw AssertionError("Condition not met")
                    }
                }
            } else if (px < nx) {
                while (node?.prev != null) {
                    node = node.prev
                    if (point == node?.point) {
                        break
                    }
                }
            } else {
                while (node?.next != null) {
                    node = node.next
                    if (point == node?.point)
                        break
                }
            }
            if(node != null) searchNode = node
            return node
        }
    }

    // Initial triangle factor, seed triangle will extend 30% of
// PointSet width to both left and right.
    const val kAlpha = 0.3

    data class Basin(var leftNode: Node? = null,
                     var bottomNode: Node? = null,
                     var rightNode: Node? = null,
                     var width: Double = 0.0,
                     var leftHighest: Boolean = false) {
        fun clear() {
            leftNode = null
            bottomNode = null
            rightNode = null
            width = 0.0
            leftHighest = false
        }
    }

    data class EdgeEvent(var constrainedEdge: Edge? = null, var right: Boolean = false)

    class SweepContext(polyline: MutableList<Point>) {

        private var triangles_: MutableList<Triangle> = mutableListOf()
        private var map_: MutableList<Triangle> = mutableListOf()
        private var points_: MutableList<Point> = polyline

        // Advancing front
        private var front_: AdvancingFront? = null
        // head point used with advancing front
        private var head_: Point? = null
        // tail point used with advancing front
        private var tail_: Point? = null

        private var afHead_: Node? = null
        private var afMiddle_: Node? = null
        private var afTail_: Node? = null

        var edgeList = mutableListOf<Edge>()

        var basin = Basin()
        var edgeEvent = EdgeEvent()

        init {
            initEdges(points_)
        }

        val front: AdvancingFront?
            get() = front_

        val pointCount: Int
            get() = points_.size

        fun setHead(p1: Point) {
            head_ = p1
        }

        fun head(): Point? {
            return head_
        }

        fun setTail(p1: Point) {
            tail_ = p1
        }

        fun tail(): Point? {
            return tail_
        }

        fun addHole(polyline: MutableList<Point>, closed: Boolean) {
            initEdges(polyline, closed)
            points_.addAll(polyline)
        }

        fun addPoint(point: Point) {
            points_.add(point)
        }

        fun getTriangles(): MutableList<Triangle> {
            return triangles_
        }

        fun getMap(): MutableList<Triangle> {
            return map_
        }

        internal fun initTriangulation() {
            var xmax = points_[0].x
            var xmin = points_[0].x
            var ymax = points_[0].y
            var ymin = points_[0].y

            // Calculate bounds.
            points_.forEach { point ->
                if (point.x > xmax)
                    xmax = point.x
                if (point.x < xmin)
                    xmin = point.x
                if (point.y > ymax)
                    ymax = point.y
                if (point.y < ymin)
                    ymin = point.y
            }

            val dx = kAlpha * (xmax - xmin)
            val dy = kAlpha * (ymax - ymin)
            head_ = Point(xmin - dx, ymin - dy)
            tail_ = Point(xmax + dx, ymin - dy)

            // Sort points along y-axis
            points_.sortWith(compareBy { it.y })
        }

        private fun initEdges(polyline: MutableList<Point>, closed: Boolean = true) {
            val numPoints = polyline.size
            for (i in 0 until (if (closed) numPoints else numPoints - 1)) {
                val j = if (i < numPoints - 1) i + 1 else 0
                edgeList.add(Edge(polyline[i], polyline[j]))
            }
        }

        fun getPoint(index: Int): Point {
            return points_[index]
        }

        fun addToMap(triangle: Triangle) {
            map_.add(triangle)
        }

        fun locateNode(point: Point): Node? {
            // TODO implement search tree
            return front_!!.locateNode(point.x)
        }

        fun createAdvancingFront() {
            // Initial triangle
            val triangle = Triangle(points_[0], head_!!, tail_!!)
            map_.add(triangle)

            afHead_ = Node(triangle.getPoint(1), triangle)
            afMiddle_ = Node(triangle.getPoint(0), triangle)
            afTail_ = Node(triangle.getPoint(2))
            front_ = AdvancingFront(afHead_!!, afTail_!!)

            // TODO: More intuitive if head is middles next and not previous?
            //       so swap head and tail
            afHead_!!.next = afMiddle_
            afMiddle_!!.next = afTail_
            afMiddle_!!.prev = afHead_
            afTail_!!.prev = afMiddle_
        }

        fun removeNode(node: Node) {
            // TODO implement node removal
        }

        fun mapTriangleToNodes(t: Triangle) {
            for (i in 0 until 3) {
                if (t.getNeighbor(i) == null) {
                    val n = front_!!.locatePoint(t.pointCW(t.getPoint(i))!!)
                    if (n != null)
                        n.triangle = t
                }
            }
        }

        fun removeFromMap(triangle: Triangle) {
            map_.remove(triangle)
        }

        fun meshClean(triangle: Triangle) {
            val triangles = mutableListOf(triangle)

            while (triangles.isNotEmpty()) {
                val t = triangles.removeLast()

                if (t != null && !t.isInterior()) {
                    t.interior = true
                    triangles_.add(t)
                    for (i in 0 until 3) {
                        if (!t.constrainedEdge[i])
                            triangles.add(t.getNeighbor(i)!!)
                    }
                }
            }
        }
    }

    class Sweep(private val tcx: SweepContext) {

        fun triangulate() {
            tcx.initTriangulation()
            tcx.createAdvancingFront()
            sweepPoints()
            finalizationPolygon()
        }

        private fun sweepPoints() {
            for (i in 1 until tcx.pointCount) {
                val point = tcx.getPoint(i)
                val node = pointEvent(point)
                for (j in point.edgeList) {
                    edgeEvent(j, node)
                }
            }
        }

        private fun finalizationPolygon() {
            var t = tcx.front!!.head.next!!.triangle
            var p = tcx.front!!.head.next!!.point
            while (t != null && !t.getConstrainedEdgeCW(p)) {
                t = t.neighborCCW(p)
            }
            if (t != null) {
                tcx.meshClean(t)
            }
        }

        private fun pointEvent(point: Point): Node {
            val node = tcx.locateNode(point)
            if (node == null || node.point == null || node.next == null || node.next?.point == null) {
                throw RuntimeException("PointEvent - null node")
            }
            val newNode = newFrontTriangle(point, node)
            if (point.x <= node.point.x + EPSILON) {
                fill(node)
            }
            fillAdvancingFront(newNode)
            return newNode
        }

        private fun edgeEvent(edge: Edge, node: Node?) {
            tcx.edgeEvent.constrainedEdge = edge
            tcx.edgeEvent.right = edge.p.x > edge.q.x
            val t = node?.triangle
            if (t != null && isEdgeSideOfTriangle(t, edge.p, edge.q)) {
                return
            }
            fillEdgeEvent(edge, t!!, node!!)
            if (edge.p == tcx.edgeEvent.constrainedEdge!!.q && edge.q == tcx.edgeEvent.constrainedEdge!!.p) {
                t.markConstrainedEdge(edge.p, edge.q)
                t.neighborAcross(edge.p)?.markConstrainedEdge(edge.p, edge.q)
                legalize(t)
                legalize(t.neighborAcross(edge.p))
            }
        }

        private fun isEdgeSideOfTriangle(triangle: Triangle, ep: Point, eq: Point): Boolean {
            val index = triangle.edgeIndex(ep, eq)
            if (index != -1) {
                triangle.markConstrainedEdge(index)
                val t = triangle.getNeighbor(index)
                if (t != null) {
                    t.markConstrainedEdge(ep, eq)
                }
                return true
            }
            return false
        }

        val nodes = arrayListOf<Node>()

        private fun newFrontTriangle(point: Point, node: Node): Node {
            val triangle = Triangle(point, node.point, node.next!!.point)
            triangle.markNeighbor(node.triangle!!)
            tcx.addToMap(triangle)
            val newNode = Node(point)
            nodes.add(newNode)
            newNode.next = node.next
            newNode.prev = node
            node.next!!.prev = newNode
            node.next = newNode
            if (!legalize(triangle)) {
                tcx.mapTriangleToNodes(triangle)
            }
            return newNode
        }

        private fun fill(node: Node) {
            val triangle = Triangle(node.prev!!.point, node.point, node.next.point)
            triangle.markNeighbor(node.prev!!.triangle!!)
            triangle.markNeighbor(node.triangle!!)
            tcx.addToMap(triangle)
            node.prev!!.next = node.next
            node.next!!.prev = node.prev
            if (!legalize(triangle)) {
                tcx.mapTriangleToNodes(triangle)
            }
        }

        private fun fillAdvancingFront(n: Node) {
            var node: Node? = n.next
            while (node != null && node.next != null) {
                if (largeHoleDontFill(node)) break
                fill(node)
                node = node.next
            }
            node = n.prev
            while (node != null && node.prev != null) {
                if (largeHoleDontFill(node)) break
                fill(node)
                node = node.prev
            }
            if (n.next != null && n.next!!.next != null) {
                val angle = basinAngle(n)
                if (angle < PI_3div4) {
                    fillBasin(n)
                }
            }
        }

        private fun largeHoleDontFill(node: Node): Boolean {
            val nextNode = node.next
            val prevNode = node.prev
            if (!angleExceeds90Degrees(node.point, nextNode!!.point, prevNode!!.point)) {
                return false
            }
            if (angleIsNegative(node.point, nextNode.point, prevNode.point)) {
                return true
            }
            val next2Node = nextNode.next
            if (next2Node != null && !angleExceedsPlus90DegreesOrIsNegative(node.point, next2Node.point, prevNode.point)) {
                return false
            }
            val prev2Node = prevNode.prev
            return !(prev2Node != null && !angleExceedsPlus90DegreesOrIsNegative(node.point, nextNode.point, prev2Node.point))
        }

        private fun angleIsNegative(origin: Point, pa: Point, pb: Point): Boolean {
            val angle = angle(origin, pa, pb)
            return angle < 0
        }

        private fun angleExceeds90Degrees(origin: Point, pa: Point, pb: Point): Boolean {
            val angle = angle(origin, pa, pb)
            return angle > PI_div2 || angle < -PI_div2
        }

        private fun angleExceedsPlus90DegreesOrIsNegative(origin: Point, pa: Point, pb: Point): Boolean {
            val angle = angle(origin, pa, pb)
            return angle > PI_div2 || angle < 0
        }

        private fun angle(origin: Point, pa: Point, pb: Point): Double {
            val px = origin.x
            val py = origin.y
            val ax = pa.x - px
            val ay = pa.y - py
            val bx = pb.x - px
            val by = pb.y - py
            val x = ax * by - ay * bx
            val y = ax * bx + ay * by
            return kotlin.math.atan2(x, y)
        }

        private fun basinAngle(node: Node): Double {
            val ax = node.point.x - node.next!!.next!!.point.x
            val ay = node.point.y - node.next!!.next!!.point.y
            return kotlin.math.atan2(ay, ax)
        }

        private fun fillBasin(n: Node) {
            val leftNode: Node?
            leftNode = if (Orient2d(*n.point, *n.next!!.point, *n.next!!.next!!.point) == CCW) {
                n.next!!.next
            } else {
                n.next
            }
            tcx.basin.leftNode = leftNode
            tcx.basin.bottomNode = leftNode
            while (tcx.basin.bottomNode!!.next != null && tcx.basin.bottomNode!!.point.y >= tcx.basin.bottomNode!!.next!!.point.y) {
                tcx.basin.bottomNode = tcx.basin.bottomNode!!.next
            }
            if (tcx.basin.bottomNode == leftNode) {
                return
            }
            tcx.basin.rightNode = tcx.basin.bottomNode
            while (tcx.basin.rightNode!!.next != null && tcx.basin.rightNode!!.point.y < tcx.basin.rightNode!!.next!!.point.y) {
                tcx.basin.rightNode = tcx.basin.rightNode!!.next
            }
            if (tcx.basin.rightNode == tcx.basin.bottomNode) {
                return
            }
            tcx.basin.width = tcx.basin.rightNode!!.point.x - tcx.basin.leftNode!!.point.x
            tcx.basin.leftHighest = tcx.basin.leftNode!!.point.y > tcx.basin.rightNode!!.point.y
            fillBasinReq(tcx.basin.bottomNode!!)
        }

        private fun fillBasinReq(node: Node) {
            if (isShallow(node)) {
                return
            }
            fill(node)
            var nextNode = node.next
            while (nextNode != null && nextNode.next != null && !isShallow(nextNode.next!!)) {
                fill(nextNode)
                nextNode = nextNode.next
            }
            if (nextNode != null) {
                if (nextNode.next == null) {
                    assert(nextNode.prev == node)
                    flipScanEdgeEvent(tcx, *nextNode.point, *node.point, *node.triangle!!, *nextNode.triangle!!, *nextNode.prev!!.point)
                } else {
                    // Next is not last node
                    assert(nextNode.next != null)
                    if (Orient2d(*node.point, *nextNode.point, *nextNode.next!!.point) == CW) {
                        // Concave
                        fillBasinReq(nextNode)
                    } else {
                        // Convex
                    }
                }
            }
        }

        private fun isShallow(node: Node): Boolean {
            val height: Double
            if (tcx.basin.leftHighest) {
                height = tcx.basin.leftNode!!.point.y - node.point.y
            } else {
                height = tcx.basin.rightNode!!.point.y - node.point.y
            }
            return tcx.basin.width > height
        }

        private fun flipEdgeEvent(ep: Point, eq: Point, t: Triangle, p: Point) {
            val ot = t.neighborAcross(p) ?: throw RuntimeException("FlipEdgeEvent - null neighbor across")
            val op = ot.oppositePoint(t, p) ?: throw RuntimeException("FlipEdgeEvent - null opposing point")
            if (inScanArea(p, t.pointCCW(p)!!, t.pointCW(p)!!, op)) {
                rotateTrianglePair(t, p, ot, op)
                tcx.mapTriangleToNodes(t)
                tcx.mapTriangleToNodes(ot)

                if (p == eq && op == ep) {
                    if (eq == tcx.edgeEvent.constrainedEdge?.q && ep == tcx.edgeEvent.constrainedEdge?.p) {
                        t.markConstrainedEdge(ep, eq)
                        ot.markConstrainedEdge(ep, eq)
                        legalize(t)
                        legalize(ot)
                    } else {
                        // XXX: I think one of the triangles should be legalized here?
                    }
                } else {
                    val o = orient2d(eq, op, ep)
                    val newT = nextFlipTriangle(o, t, ot, p, op)
                    flipEdgeEvent(ep, eq, newT, p)
                }
            } else {
                val newP = nextFlipPoint(ep, eq, ot, op)
                flipScanEdgeEvent(ep, eq, t, ot, newP)
                edgeEvent(ep, eq, t, p)
            }
        }

        private fun nextFlipTriangle(o: Int, t: Triangle, ot: Triangle, p: Point, op: Point): Triangle {
            return if (o == CCW) {
                ot.delaunayEdge[ot.edgeIndex(p, op)] = true
                legalize(ot)
                ot.clearDelunayEdges()
                t
            } else {
                t.delaunayEdge[t.edgeIndex(p, op)] = true
                legalize(t)
                t.clearDelunayEdges()
                ot
            }
        }

        private fun nextFlipPoint(ep: Point, eq: Point, ot: Triangle, op: Point): Point {
            val o2d = orient2d(eq, op, ep)
            return if (o2d == CW) {
                ot.pointCCW(op)!!
            } else if (o2d == CCW) {
                ot.pointCW(op)!!
            } else {
                throw RuntimeException("[Unsupported] Opposing point on constrained edge")
            }
        }

        private fun flipScanEdgeEvent(ep: Point, eq: Point, flipTriangle: Triangle, t: Triangle, ot: Triangle, newP: Point) {
            val op = ot.oppositePoint(t, newP) ?: throw RuntimeException("FlipScanEdgeEvent - null opposing point")
            val p1 = flipTriangle.pointCCW(eq) ?: throw RuntimeException("FlipScanEdgeEvent - null on either of points")
            val p2 = flipTriangle.pointCW(eq) ?: throw RuntimeException("FlipScanEdgeEvent - null on either of points")
            if (inScanArea(eq, p1, p2, op)) {
                flipEdgeEvent(eq, op, ot, op)
                val newT = nextFlipTriangle(orient2d(eq, op, ep), flipTriangle, ot, newP, op)
                flipScanEdgeEvent(ep, eq, newT, ot, newP)
            }
        }

        private fun inScanArea(a: Point, b: Point, c: Point, p: Point): Boolean {
            val o1 = orient2d(b, a, p)
            val o2 = orient2d(c, b, p)
            val o3 = orient2d(a, c, p)

            return if (o1 == CW && o2 == CW && o3 == CW) {
                true
            } else if (o1 == CCW && o2 == CCW && o3 == CCW) {
                false
            } else {
                false
            }
        }

        private fun orient2d(pa: Point, pb: Point, pc: Point): Int {
            val detleft = (pa.x - pc.x) * (pb.y - pc.y)
            val detright = (pa.y - pc.y) * (pb.x - pc.x)
            val valcmp = detleft.compareTo(detright)
            return when {
                valcmp < 0 -> CW
                valcmp > 0 -> CCW
                else -> 0
            }
        }

        private fun legalize(t: Triangle): Boolean {
            for (i in 0..2) {
                if (t.delaunayEdge[i]) continue

                val ot = t.neighbors[i] ?: continue
                val p = t.getPoint(i)
                val op = ot.oppositePoint(t, p) ?: continue

                if (inCircle(p, t.pointCCW(p) ?: continue, t.pointCW(p) ?: continue, op)) {
                    val ci = t.edgeIndex(p, op)
                    t.delaunayEdge[ci] = true
                    ot.delaunayEdge[ci] = true

                    rotateTrianglePair(t, p, ot, op)
                    tcx.mapTriangleToNodes(t)
                    tcx.mapTriangleToNodes(ot)

                    if (p == tcx.edgeEvent.constrainedEdge?.q && op == tcx.edgeEvent.constrainedEdge?.p) {
                        t.markConstrainedEdge(p, op)
                        ot.markConstrainedEdge(p, op)
                        legalize(t)
                        legalize(ot)
                    } else {
                        val o = orient2d(eq, op, ep)
                        val newT = nextFlipTriangle(o, t, ot, p, op)
                        flipEdgeEvent(ep, eq, newT, p)
                    }

                    return true
                }
            }
            return false
        }

        private fun inCircle(pa: Point, pb: Point, pc: Point, pd: Point): Boolean {
            val adx = pa.x - pd.x
            val ady = pa.y - pd.y
            val bdx = pb.x - pd.x
            val bdy = pb.y - pd.y

            val adxbdy = adx * bdy
            val bdxady = bdx * ady
            val oabd = adxbdy - bdxady

            if (oabd <= 0) return false

            val cdx = pc.x - pd.x
            val cdy = pc.y - pd.y

            val cdxady = cdx * ady
            val adxcdy = adx * cdy
            val ocad = cdxady - adxcdy

            if (ocad <= 0) return false

            val bdxcdy = bdx * cdy
            val cdxbdy = cdx * bdy

            val alift = adx * adx + ady * ady
            val blift = bdx * bdx + bdy * bdy
            val clift = cdx * cdx + cdy * cdy

            val det = alift * (bdxcdy - cdxbdy) + blift * ocad + clift * oabd

            return det > 0
        }

        private fun flipTrianglePair(a: Triangle, b: Point, c: Triangle, d: Point) {
            val n1: Triangle?
            if (b == a.pointCCW(d)!!) {
                n1 = a.neighborCW(b)
                a.clearNeighborCW()
                if (n1 != null)
                    n1.markNeighborTriangle(a)
            } else {
                n1 = a.neighborCCW(b)
                a.clearNeighborCCW()
                if (n1 != null)
                    n1.markNeighborTriangle(a)
            }

            val n2: Triangle?
            if (d == c.pointCCW(b)!!) {
                n2 = c.neighborCW(d)
                c.clearNeighborCW()
                if (n2 != null)
                    n2.markNeighborTriangle(c)
            } else {
                n2 = c.neighborCCW(d)
                c.clearNeighborCCW()
                if (n2 != null)
                    n2.markNeighborTriangle(c)
            }

            a.markNeighborTriangle(c)
            c.markNeighborTriangle(a)
            b.flipTriangleNeighbor(c, n1)
            d.flipTriangleNeighbor(a, n2)
        }

        private fun edgeEvent(ep: Point, eq: Point, t: Triangle, p: Point) {
            tcx.edgeEvent.constrainedEdge = tcx.edgeEvent.edge!!.enclosingEdge
            tcx.edgeEvent.right = tcx.edgeEvent.edge.p.x > tcx.edgeEvent.edge.q.x

            if (isEdgeSideOfTriangle(t, ep, eq)) {
                return
            }
            fillEdgeEvent(tcx.edgeEvent.edge, t, p)
            edgeEvent(ep, eq, t, p)
        }

        private fun fillEdgeEvent(edge: Edge, t: Triangle, p: Point?) {
            tcx.edgeEvent.constrainedEdge = edge
            tcx.edgeEvent.right = edge.p.x > edge.q.x

            val ot = t.neighborAcross(p) ?: throw RuntimeException("FillEdgeEvent - null neighbor across")

            if (isEdgeSideOfTriangle(ot, edge.p, edge.q)) {
                return
            }

            fillEdgeEvent(edge, ot, ot.oppositePoint(t, p)!!)
        }

        private fun fillEdgeEvent(edge: Edge, ot: Triangle, op: Point) {
            if (op == edge.q) {
                return
            }

            if (orient2d(edge.q, op, edge.p) == CW) {
                fillRightConvexEdgeEvent(tcx, edge, ot, op)
            } else {
                val newT = nextFlipTriangle(orient2d(ep, eq, op), edge.triangle!!, ot, p, op)
                flipEdgeEvent(ep, eq, newT, p)
            }
        }

        private fun fillRightConvexEdgeEvent(tcx: SweepContext, edge: Edge, node: Node) {
            val o2d = orient2d(node.next!!.point, node.next!!.next!!.point, node.next!!.next!!.next!!.point)
            if (o2d == CW) {
                fillRightConcaveEdgeEvent(tcx, edge, node.next!!)
            } else {
                if (orient2d(edge.q, node.next!!.next!!.point, edge.p) == CW) {
                    fillRightConvexEdgeEvent(tcx, edge, node.next!!)
                } else {
                    // Above
                }
            }
        }

        private fun fillRightConcaveEdgeEvent(tcx: SweepContext, edge: Edge, node: Node) {
            fill(node.next!!)
            if (node.next!!.point != edge.p) {
                if (orient2d(edge.q, node.next!!.point, edge.p) == CW) {
                    if (orient2d(node.point, node.next!!.point, node.next!!.next!!.point) == CW) {
                        fillRightConcaveEdgeEvent(tcx, edge, node.next!!)
                    } else {
                        // Next is convex
                    }
                }
            }
        }

        private fun fillRightBelowEdgeEvent(tcx: SweepContext, edge: Edge, node: Node) {
            if (node.point.x < edge.p.x) {
                if (orient2d(node.point, node.next!!.point, node.prev!!.point) == CW) {
                    // Concave
                    fillRightConcaveEdgeEvent(tcx, edge, node)
                } else {
                    // Convex
                    fillRightConvexEdgeEvent(tcx, edge, node)
                    fillRightBelowEdgeEvent(tcx, edge, node)
                }
            }
        }

        private fun fillRightAboveEdgeEvent(tcx: SweepContext, edge: Edge, node: Node?) {
            while (node != null && node.next!!.point.x < edge.p.x) {
                if (orient2d(edge.q, node.next!!.point, edge.p) == CCW) {
                    fillRightBelowEdgeEvent(tcx, edge, node)
                } else {
                    node = node.next
                }
            }
        }

        private fun fillLeftConcaveEdgeEvent(tcx: SweepContext, edge: Edge, node: Node) {
            fill(node.prev!!)
            if (node.prev!!.point != edge.p) {
                if (orient2d(edge.q, node.prev!!.point, edge.p) == CCW) {
                    if (orient2d(node.point, node.prev!!.point, node.prev!!.prev!!.point) == CCW) {
                        fillLeftConcaveEdgeEvent(tcx, edge, node.prev!!)
                    } else {
                        // Next is convex
                    }
                }
            }
        }

        private fun fillLeftConvexEdgeEvent(tcx: SweepContext, edge: Edge, node: Node) {
            if (orient2d(node.prev!!.point, node.prev!!.prev!!.point, node.prev!!.prev!!.prev!!.point) == CCW) {
                fillLeftConcaveEdgeEvent(tcx, edge, node.prev!!)
            } else {
                if (orient2d(edge.q, node.prev!!.prev!!.point, edge.p) == CCW) {
                    // Below
                    fillLeftConvexEdgeEvent(tcx, edge, node.prev!!)
                } else {
                    // Above
                }
            }
        }

        private fun fillLeftBelowEdgeEvent(tcx: SweepContext, edge: Edge, node: Node) {
            if (node.point.x > edge.p.x) {
                if (orient2d(node.point, node.prev!!.point, node.prev!!.prev!!.point) == CCW) {
                    // Concave
                    fillLeftConcaveEdgeEvent(tcx, edge, node)
                } else {
                    // Convex
                    fillLeftConvexEdgeEvent(tcx, edge, node)
                    fillLeftBelowEdgeEvent(tcx, edge, node)
                }
            }
        }

        private fun fillLeftAboveEdgeEvent(tcx: SweepContext, edge: Edge, node: Node?) {
            while (node != null && node.prev!!.point.x > edge.p.x) {
                if (orient2d(edge.q, node.prev!!.point, edge.p) == CW) {
                    fillLeftBelowEdgeEvent(tcx, edge, node)
                } else {
                    node = node.prev
                }
            }
        }

        private fun legalize(t: Triangle?): Boolean {
            if (t == null) return false

            for (i in 0..2) {
                if (t.delaunayEdge[i]) continue

                val ot = t.neighbors[i] ?: continue
                val p = t.getPoint(i)
                val op = ot.oppositePoint(t, p) ?: continue

                if (inCircle(p, t.pointCCW(p) ?: continue, t.pointCW(p) ?: continue, op)) {
                    val ci = t.edgeIndex(p, op)
                    t.delaunayEdge[ci] = true
                    ot.delaunayEdge[ci] = true

                    rotateTrianglePair(t, p, ot, op)
                    tcx.mapTriangleToNodes(t)
                    tcx.mapTriangleToNodes(ot)

                    if (p == tcx.edgeEvent.constrainedEdge?.q && op == tcx.edgeEvent.constrainedEdge?.p) {
                        t.markConstrainedEdge(p, op)
                        ot.markConstrainedEdge(p, op)
                        legalize(t)
                        legalize(ot)
                    } else {
                        edgeEvent(tcx.edgeEvent.constrainedEdge!!.p, tcx.edgeEvent.constrainedEdge!!.q, t, p)
                    }
                    return true
                }
            }
            return false
        }

        private fun inCircle(pa: Point, pb: Point, pc: Point, pd: Point): Boolean {
            val adx = pa.x - pd.x
            val ady = pa.y - pd.y
            val bdx = pb.x - pd.x
            val bdy = pb.y - pd.y

            val adxbdy = adx * bdy
            val bdxady = bdx * ady
            val oabd = adxbdy - bdxady

            if (oabd <= 0) return false

            val cdx = pc.x - pd.x
            val cdy = pc.y - pd.y

            val cdxady = cdx * ady
            val adxcdy = adx * cdy
            val ocad = cdxady - adxcdy

            if (ocad <= 0) return false

            val bdxcdy = bdx * cdy
            val cdxbdy = cdx * bdy

            val alift = adx * adx + ady * ady
            val blift = bdx * bdx + bdy * bdy
            val clift = cdx * cdx + cdy * cdy

            val det = alift * (bdxcdy - cdxbdy) + blift * ocad + clift * oabd

            return det > 0
        }

        private fun rotateTrianglePair(t: Triangle, p: Point, ot: Triangle, op: Point) {
            val n1: Triangle?
            if (p == t.pointCCW(op)!!) {
                n1 = t.neighborCW(p)
                t.clearNeighborCW()
                if (n1 != null)
                    n1.markNeighborTriangle(t)
            } else {
                n1 = t.neighborCCW(p)
                t.clearNeighborCCW()
                if (n1 != null)
                    n1.markNeighborTriangle(t)
            }

            val n2: Triangle?
            if (op == ot.pointCCW(p)!!) {
                n2 = ot.neighborCW(op)
                ot.clearNeighborCW()
                if (n2 != null)
                    n2.markNeighborTriangle(ot)
            } else {
                n2 = ot.neighborCCW(op)
                ot.clearNeighborCCW()
                if (n2 != null)
                    n2.markNeighborTriangle(ot)
            }

            t.markNeighborTriangle(ot)
            ot.markNeighborTriangle(t)
            p.flipTriangleNeighbor(ot, n1)
            op.flipTriangleNeighbor(t, n2)
        }

        companion object {
            private const val CW = 1
            private const val CCW = -1
            private const val PI_div4 = kotlin.math.PI / 4
            private const val PI_div2 = kotlin.math.PI / 2
            private const val PI_3div4 = 3 * kotlin.math.PI / 4
            private const val EPSILON = 1e-12
        }
    }

    enum class Orientation { CW, CCW, COLLINEAR };

    fun Orient2d(pa: Point, pb: Point, pc: Point): Orientation
    {
        val detleft = (pa.x - pc.x) * (pb.y - pc.y);
        val detright = (pa.y - pc.y) * (pb.x - pc.x);
        val `val` = detleft - detright;

        // Using a tolerance here fails on concave-by-subepsilon boundaries
        //   if (val > -EPSILON && val < EPSILON) {
        // Using == on double makes -Wfloat-equal warnings yell at us
        if (`val` == 0.0) {
            return Orientation.COLLINEAR;
        } else if (`val` > 0) {
            return Orientation.CCW;
        }
        return Orientation.CW;
    }
}
*/
