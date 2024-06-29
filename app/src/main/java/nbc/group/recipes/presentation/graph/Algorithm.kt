package nbc.group.recipes.presentation.graph

import kotlin.math.pow
import kotlin.math.sqrt


const val COULOMB = 4800f
const val DISTANCE = 3200f //3200f
const val GRAVITY = 0.04f
const val BOUNCE = 0.06f
const val ATTENUATION = 0.4f

fun operate(
    nodes: MutableList<Node<Any>>,
    edges: MutableList<Edge>,
    inInteraction: Int,
) {
    for(i in 0 until nodes.size) {
        if(inInteraction == i) continue
        val ithNode = nodes[i]
        var fx = 0f
        var fy = 0f

        for(j in 0 until nodes.size) {
            val jthNode = nodes[j]

            val distX = ithNode.x - jthNode.x
            val distY = ithNode.y - jthNode.y

            val rsq = distX.pow(2) + distY.pow(2)

            val coulombDistX = COULOMB * distX
            val coulombDistY = COULOMB * distY

            if(rsq >= 0.01f && sqrt(rsq) < DISTANCE) {
                fx += coulombDistX / rsq
                fy += coulombDistY / rsq
            }
        }

        val distXC = -1 * (ithNode.x + ithNode.size / 2) + 500
        val distYC = -1 * (ithNode.y + ithNode.size / 2) + 500
        fx += GRAVITY * distXC
        fy += GRAVITY * distYC

        for(j in 0 until edges.size) {
            val jthEdge = edges[j]

            var distX = 0f
            var distY = 0f

            if(i == jthEdge.to) {
                val targetNode = nodes[jthEdge.from]
                distX = targetNode.x - ithNode.x
                distY = targetNode.y - ithNode.y
            } else if(i == jthEdge.from) {
                val targetNode = nodes[jthEdge.to]
                distX = targetNode.x - ithNode.x
                distY = targetNode.y - ithNode.y
            }

            fx += BOUNCE * distX
            fy += BOUNCE * distY
        }

        val dx = (ithNode.dx + fx) * ATTENUATION
        val dy = (ithNode.dy + fy) * ATTENUATION

        val x = ithNode.x + dx
        val y = ithNode.y + dy

        nodes[i] = ithNode.copy(
            x = x,
            y = y,
            dx = dx,
            dy = dy
        )
    }
}