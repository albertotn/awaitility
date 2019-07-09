/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.awaitility;

import org.awaitility.classes.Asynch;
import org.awaitility.classes.FakeRepository;
import org.awaitility.classes.FakeRepositoryImpl;
import org.awaitility.core.ConditionEvaluationLogger;
import org.awaitility.pollinterval.FibonacciPollInterval;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.FIVE_HUNDRED_MILLISECONDS;
import static org.awaitility.Durations.TWO_HUNDRED_MILLISECONDS;
import static org.awaitility.core.ConditionEvaluationLogger.conditionEvaluationLogger;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;
import static org.awaitility.pollinterval.FixedPollInterval.fixed;
import static org.awaitility.pollinterval.IterativePollInterval.iterative;

/**
 * Tests for await().until(Runnable) using AssertionCondition.
 *
 * @author Marcin Zajączkowski, 2014-03-28
 * @author Johan Haleby
 */
public class PollIntervalTest {

    private FakeRepository fakeRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        fakeRepository = new FakeRepositoryImpl();
        Awaitility.reset();
    }

    @Test(timeout = 2000)
    public void fibonacciPollInterval() {
        new Asynch(fakeRepository).perform();
        await().with().conditionEvaluationListener(new ConditionEvaluationLogger()).pollInterval(new FibonacciPollInterval()).untilAsserted(() -> assertThat(fakeRepository.getValue()).isEqualTo(1));
    }

    @Test(timeout = 2000)
    public void fibonacciPollIntervalStaticallyImported() {
        new Asynch(fakeRepository).perform();
        await().with().conditionEvaluationListener(conditionEvaluationLogger()).
                pollInterval(fibonacci().with().offset(10).and().unit(MILLISECONDS)).
                untilAsserted(() -> assertThat(fakeRepository.getValue()).isEqualTo(1));
    }

    @Test(timeout = 2000)
    public void inlinePollInterval() {
        new Asynch(fakeRepository).perform();
        await().with().conditionEvaluationListener(new ConditionEvaluationLogger()).
                pollInterval((__, previous) -> previous.multipliedBy(2).plusMillis(1)).
                untilAsserted(() -> assertThat(fakeRepository.getValue()).isEqualTo(1));
    }

    @Test(timeout = 2000)
    public void iterativePollInterval() {
        new Asynch(fakeRepository).perform();
        await().with().conditionEvaluationListener(new ConditionEvaluationLogger()).
                pollInterval(iterative(duration -> duration.multipliedBy(2), FIVE_HUNDRED_MILLISECONDS)).
                untilAsserted(() -> assertThat(fakeRepository.getValue()).isEqualTo(1));
    }

    @Test(timeout = 2000)
    public void fixedPollInterval() {
        new Asynch(fakeRepository).perform();
        await().with().conditionEvaluationListener(new ConditionEvaluationLogger()).
                pollInterval(fixed(TWO_HUNDRED_MILLISECONDS)).
                untilAsserted(() -> assertThat(fakeRepository.getValue()).isEqualTo(1));
    }
}
