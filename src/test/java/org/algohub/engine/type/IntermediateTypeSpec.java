package org.algohub.engine.type;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntermediateTypeSpec {

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionForUnknownType() throws Exception {
        IntermediateType.fromString("dummy");
    }

    @Test
    public void returnsInternalValueAsStringRepresentation() throws Exception {
        assertThat(IntermediateType.INT.toString()).isEqualTo("int");
    }
}