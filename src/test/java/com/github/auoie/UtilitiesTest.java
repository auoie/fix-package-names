package com.github.auoie;

import static org.junit.jupiter.api.Assertions.*;

import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;

class UtilitiesTest {
  @Test
  void testFixDirectoryName() {
    Slugify slg = Slugify.builder().underscoreSeparator(true).build();
    assertEquals("_1_hello_hi", Utilities.getNewDirectoryName(slg, "1-Hello hi"));
  }
}
