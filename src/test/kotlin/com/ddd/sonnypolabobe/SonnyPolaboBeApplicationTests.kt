package com.ddd.sonnypolabobe

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

//@SpringBootTest
class SonnyPolaboBeApplicationTests {

    @Test
    fun contextLoads() {
        println(solution(2, 10, intArrayOf(7,4,5,6))) // 8
//        println(solution(arrayOf(
//            intArrayOf(1,2,1),
//            intArrayOf(8,2,0),
//            intArrayOf(1,7,2)
//        ), intArrayOf(0, 0))) // true
//        println(solution(arrayOf(
//            intArrayOf(1,2,3,2,1),
//            intArrayOf(4,2,0,7,2),
//            intArrayOf(1,3,3,8,1),
//            intArrayOf(2,0,1,1,1),
//            intArrayOf(8,2,8,1,1)
//        ), intArrayOf(0, 0))) // false

//        println(solution(arrayOf(
//            intArrayOf(1,2,3,2,1),
//            intArrayOf(4,2,0,7,1),
//            intArrayOf(1,3,2,8,1),
//            intArrayOf(2,0,1,1,1),
//            intArrayOf(8,2,1,2,1)
//        ), intArrayOf(4,3)
//        )) // true
//        println(solution(
//            intArrayOf(23),  // 고객 수
//            intArrayOf(12, 3, 19),   // 모델 처리량
//            intArrayOf(28, 10, 35)   // 모델 비용
//        ))

    }

    fun solution(bridge_length: Int, weight: Int, truck_weights: IntArray): Int {
        var answer = 0

        val queue: Queue<Int> = LinkedList()
        var totalWeight = 0

        for(truck in truck_weights) {
            queue.add(truck)
        }

        val bridge = LinkedList(List(bridge_length) { 0 })

        while(bridge.isNotEmpty()) {
            answer++

            totalWeight -= bridge.poll() // 다리를 건넌 트럭의 무게를 빼준다.

            if(queue.isNotEmpty()) {
                val nextWeight = queue.peek()
                if(nextWeight + totalWeight <= weight ) {
                    totalWeight += nextWeight
                    bridge.add(queue.poll())
                } else {
                    bridge.add(0)
                }
            }


        }


//        while (bridge.isNotEmpty()) {
//            answer++
//            totalWeight -= bridge.poll()
//
//            if (waiting.isNotEmpty()) {
//                val nextWeight = waiting.peek()
//
//                if (totalWeight + nextWeight <= weight) {
//                    totalWeight += nextWeight
//                    bridge.add(waiting.poll())
//                } else {
//                    bridge.add(0)
//                }
//            }
//        }
        return answer
    }


//    fun solution(map : Array<IntArray>, entrancePoint: IntArray) : Boolean {
//        // 출발점에서 닭가슴살을 찾을 수 있는지 여부가 answer
//        // 닭가슴살은 7
//        // 액상 과당은 0, 초콜릿은 8 이라고 할 때
//        // map에서 출발점을 기준으로는 좌우로만 갈 수 있다.
//        // 이후에는 위 아래로 갈 수 있다.
//        // 그 다음에는 좌우로만 갈 수 있다. 이 구성을 반복한다고 할 때 이동 방향으로는 현재 위치의 숫자만큼 간다.
//        // 이동의 도착점에 액상 과당이나 초콜릿이 있다면 false를 반환한다.
//        // 영역을 벗어나도 false를 반환한다.
//
//        var answer = false
//        var x = entrancePoint[0]
//        var y = entrancePoint[1]
//        var direction = if (x % 2 == 0) 0 else 2
//        var nextX = 0
//        var nextY = 0
//
//        while (true) {
//            if (map[x][y] == 7) {
//                answer = true
//                break
//            }
//            if (map[x][y] == 0 || map[x][y] == 8) {
//                break
//            }
//            when (direction) {
//                0 -> {
//                    nextX = x
//                    nextY = y + map[x][y]
//                }
//                1 -> {
//                    nextX = x + map[x][y]
//                    nextY = y
//                }
//                2 -> {
//                    nextX = x
//                    nextY = y - map[x][y]
//                }
//                3 -> {
//                    nextX = x - map[x][y]
//                    nextY = y
//                }
//            }
//            if (nextX < 0 || nextX >= map.size || nextY < 0 || nextY >= map[0].size) {
//                break
//            }
//            x = nextX
//            y = nextY
//            direction = (direction + 1) % 4 // 방향을 바꾼다.
//        }
//        return answer
//
//    }

//    fun solution(customers : IntArray, modelCapacities: IntArray, modelCosts: IntArray) : Int {
//        var answer = 0
//
//        // 매 시간 고객의 접수 건을 담고 있는 배열 customers
//        // 각 모델의 처리량을 담고 있는 배열 modelCapacities 예를 들면, A-12, B-3, C-19
//        // 각 모델의 비용을 담고 있는 배열 modelCosts 예를 들면, A-28, B-10, C-35
//        // 각 모델은 1시간에 위 처리량만큼 처리할 수 있다.
//
//        // 최소 비용으로 처리하고자 할 때, 그 비용을 반환한다.
//
//        // 모델의 처리량과 비용을 (처리량, 비용) 쌍으로 묶고, 처리량이 큰 순으로 정렬
//        val pair = modelCapacities.zip(modelCosts).sortedByDescending { it.first }
//
//        for (customer in customers) {
//            var minCost = Int.MAX_VALUE
//
//            for (i in pair.indices) {
//                val (capacity, cost) = pair[i]
//                val fullModelsNeeded = customer / capacity
//                val remainder = customer % capacity
//
//                // 총 비용 계산
//                var totalCost = cost * fullModelsNeeded
//
//                // 잔여 고객 처리 비용 추가
//                if (remainder > 0) {
//                    // 잔여 고객을 처리하기 위한 최소 비용을 계산
//                    var extraCost = Int.MAX_VALUE
//                    for (j in pair.indices) {
//                        val (extraCapacity, extraCostValue) = pair[j]
//                        if (extraCapacity >= remainder) {
//                            extraCost = minOf(extraCost, extraCostValue)
//                        }
//                    }
//                    totalCost += extraCost
//                }
//
//                // 최소 비용 업데이트
//                minCost = minOf(minCost, totalCost)
//            }
//            answer += minCost
//        }
//
//        return answer
//    }





}
