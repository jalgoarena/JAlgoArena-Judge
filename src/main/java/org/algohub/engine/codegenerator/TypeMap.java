package org.algohub.engine.codegenerator;

import com.google.common.collect.ImmutableMap;

import org.algohub.engine.type.IntermediateType;

/**
 * Map intermediate types to language related types.
 */
@SuppressWarnings({"PMD.AvoidDuplicateLiterals"})
final class TypeMap {
  /**
   * Map intermediate types to Java types.
   */
  public static final ImmutableMap<IntermediateType, String> JAVA_TYPE_MAP =
      ImmutableMap.<IntermediateType, String>builder().put(IntermediateType.BOOL, "boolean")
          .put(IntermediateType.STRING, "String").put(IntermediateType.DOUBLE, "double")
          .put(IntermediateType.INT, "int").put(IntermediateType.LONG, "long")

          //.put(IntermediateType.ARRAY, "[]")
          .put(IntermediateType.LIST, "ArrayList").put(IntermediateType.SET, "HashSet")
          .put(IntermediateType.MAP, "HashMap")

          .put(IntermediateType.LINKED_LIST_NODE, "LinkedListNode")
          .put(IntermediateType.BINARY_TREE_NODE, "BinaryTreeNode")

          .build();

  private TypeMap() {}
}
