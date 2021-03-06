/*
 * Copyright (c) 2011-2014 Pivotal Software, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package reactor.rx.action.filter;

import reactor.fn.Predicate;
import reactor.rx.action.Action;

/**
 * @author Stephane Maldini
 * @since 1.1
 */
public class FilterAction<T> extends Action<T, T> {

	public static final Predicate<Boolean> simplePredicate = new Predicate<Boolean>() {
		@Override
		public boolean test(Boolean aBoolean) {
			return aBoolean;
		}
	};

	private final Predicate<? super T> p;

	public FilterAction(Predicate<? super T> p) {
		this.p = p;
	}

	@Override
	protected void doNext(T value) {
		if (p.test(value)) {
			broadcastNext(value);
		} else {
			requestMore(1);
			// GH-154: Verbose error level logging of every event filtered out by a Stream filter
			// Fix: ignore Predicate failures and drop values rather than notifying of errors.
			//d.accept(new IllegalArgumentException(String.format("%s failed a predicate test.", value)));
		}
	}
}
