package com.noyanov.usetech_test

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    DataTestSuite::class,
    UITestSuite::class
)
class AllTestSuite
