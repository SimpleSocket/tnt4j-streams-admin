/*
 * Copyright 2014-2020 JKOOL, LLC.
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

import wrappers.HttpClientWrapper;

public class UpdateAgent {

	private static String url = "https://api.github.com/repos/Nastel/tnt4j-streams/releases";

	private HttpClientWrapper httpClientWrapper = new HttpClientWrapper();

	public void getAvailableVersions() {

	}

	public void shutdown() {
		httpClientWrapper.shutdown();
	}

}
