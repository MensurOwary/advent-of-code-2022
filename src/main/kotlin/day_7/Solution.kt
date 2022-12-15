package day_7

import java.nio.file.Files
import java.nio.file.Path

/* Parsing the input file */

sealed interface Input

data class CommandInput(val parameter: String) : Input
data class FileInput(val name: String, val size: Int) : Input
data class FolderInput(val name: String) : Input

fun String.toInput(): Input {
    return if (startsWith("$")) {
        val parameter = this
            .replace("$ cd", "")
            .trim()
        CommandInput(parameter)
    } else {
        if (startsWith("dir")) {
            FolderInput(
                name = this.replace("dir", "").trim()
            )
        } else {
            val pieces = this.split(" ")
            return FileInput(
                name = pieces[1],
                size = pieces[0].toInt()
            )
        }
    }
}

/* Constructing the tree */

open class Tree(val parent: Tree?, val name: String, val children: MutableList<Tree>) {
    open fun findSize(): Int {
        return this.children.sumOf { it.findSize() }
    }
}

class File(parent: Tree?, name: String, val size: Int) : Tree(parent, name, arrayListOf()) {
    override fun findSize() = size
}

fun folderSizeQuery(tree: Tree, predicate: (Int) -> Boolean = { true }) =
    sequence {
        val queue = arrayListOf(tree)
        while (queue.isNotEmpty()) {
            val obj = queue.removeFirst()
            val size = obj.findSize()
            if (predicate(size)) {
                yield(size)
            }
            obj.children.filterNot { it is File }.forEach { queue.add(it) }
        }
    }

fun main() {
    val inputFile = Path.of("src/main/kotlin/day_7/input")
    val lines = Files.readAllLines(inputFile)
    val inputs = lines.filterNot { it.startsWith("$ ls") }.map { it.toInput() }

    val tree = constructDirectoryTree(inputs)

    // part 1
    println("Part 1: ${folderSizeQuery(tree) { it <= 100000 }.sum()}")

    // part 2
    val totalSpace = 70000000
    val usedSpace = tree.findSize()
    val freeTargetSpace = 30000000

    val smallestToDelete = folderSizeQuery(tree)
        .filter { totalSpace - usedSpace + it >= freeTargetSpace }
        .sorted()
        .first()

    println("Part 2: $smallestToDelete")
}

private fun constructDirectoryTree(inputs: List<Input>): Tree {
    val tree = Tree(
        name = "/",
        parent = null,
        children = arrayListOf()
    )

    var currentNode = tree

    for (input in inputs) {
        when (input) {
            is CommandInput -> {
                currentNode = when (input.parameter) {
                    "/" -> tree
                    ".." -> currentNode.parent!!
                    else -> currentNode.children
                        .filterNot { it is File }
                        .first { it.name == input.parameter }
                }
            }

            is FolderInput -> {
                currentNode.children.add(
                    Tree(
                        parent = currentNode,
                        name = input.name,
                        children = arrayListOf()
                    )
                )
            }

            is FileInput -> {
                currentNode.children.add(
                    File(
                        parent = currentNode,
                        name = input.name,
                        size = input.size
                    )
                )
            }
        }
    }
    return tree
}