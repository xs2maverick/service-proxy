/* Copyright 2009 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */
package com.predic8.membrane.core.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.predic8.membrane.core.tests.http.BodyTest;
import com.predic8.membrane.core.tests.http.HeaderTest;
import com.predic8.membrane.core.tests.http.RequestTest;
import com.predic8.membrane.core.tests.http.ResponseTest;
import com.predic8.membrane.core.tests.magic.MagicTest;
import com.predic8.membrane.core.tests.util.ByteUtilTest;
import com.predic8.membrane.core.tests.util.HttpUtilTest;


public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.osmoticweb.monitor");
		//$JUnit-BEGIN$
		suite.addTestSuite(HeaderTest.class);
		suite.addTestSuite(BodyTest.class);
		suite.addTestSuite(ByteUtilTest.class);
		suite.addTestSuite(HttpUtilTest.class);
		suite.addTestSuite(RequestTest.class);
		suite.addTestSuite(ResponseTest.class);
		suite.addTestSuite(MagicTest.class);
		//$JUnit-END$
		return suite;
	}
}