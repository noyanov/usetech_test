package com.noyanov.usetech_test

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    MainActivityTest::class,
    FindBookActivityTest::class,
    BookDetailsActivityTest::class
)
class UITestSuite
