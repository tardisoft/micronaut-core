/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.reactive.rxjava2;

import io.micronaut.core.annotation.Internal;
import io.micronaut.scheduling.instrument.ReactiveInstrumenter;
import io.micronaut.scheduling.instrument.RunnableInstrumenter;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;

import java.util.Collection;
import java.util.List;

/**
 * Inspired by code in Brave. Provides general instrumentation abstraction for RxJava2.
 * See https://github.com/openzipkin/brave/tree/master/context/rxjava2/src/main/java/brave/context/rxjava2/internal.
 *
 * @param <T> The type
 * @author graemerocher
 * @since 1.1
 */
@Internal
final class RxInstrumentedConnectableObservable<T> extends ConnectableObservable<T> implements RxInstrumentedComponent {
    private final ConnectableObservable<T> source;
    private final List<RunnableInstrumenter> instrumentations;

    /**
     * Default constructor.
     * @param source The source
     * @param instrumentations The instrumentations
     */
    RxInstrumentedConnectableObservable(
            ConnectableObservable<T> source, List<RunnableInstrumenter> instrumentations) {
        this.source = source;
        this.instrumentations = instrumentations;
    }

    /**
     * Default constructor.
     * @param source The source
     * @param instrumentations The instrumentations
     */
    RxInstrumentedConnectableObservable(
            ConnectableObservable<T> source, Collection<ReactiveInstrumenter> instrumentations) {
        this.source = source;
        this.instrumentations = toRunnableInstrumenters(instrumentations);
    }

    @Override protected void subscribeActual(Observer<? super T> o) {
        source.subscribe(new RxInstrumentedObserver<>(o, instrumentations));
    }

    @Override public void connect(Consumer<? super Disposable> connection) {
        Runnable onConnect = () -> source.connect(connection);
        for (RunnableInstrumenter instrumentation : instrumentations) {
            onConnect = instrumentation.instrument(onConnect);
        }
        onConnect.run();
    }
}
