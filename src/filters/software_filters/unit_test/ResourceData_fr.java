/*
* CDDL HEADER START
*
* The contents of this file are subject to the terms of the
* Common Development and Distribution License (the "License").
* You may not use this file except in compliance with the License.
*
* You can obtain a copy of the license at LICENSE.txt
* or http://www.opensource.org/licenses/cddl1.php.
* See the License for the specific language governing permissions
* and limitations under the License.
*
* When distributing Covered Code, include this CDDL HEADER in each
* file and include the License file at LICENSE.txt.
* If applicable, add the following below this CDDL HEADER, with the
* fields enclosed by brackets "[]" replaced with your own identifying
* information: Portions Copyright [yyyy] [name of copyright owner]
*
* CDDL HEADER END
*/
import java.util.ListResourceBundle;

public class ResourceData_fr extends ListResourceBundle {
    public Object[][] getContents() {
	return contents;
    }

    static final Object[][] contents = {
	    /* @TMC@ EN MESSAGE {"hello.m", "Hello in French from ListResource"}, */
        { "hello.m", "Hello in French from ListResource" },
            // this is a comment
	    /* @TMC@ EN MESSAGE {"today.m", "Today is in French from ListResource"}, */
        { "today.m", "Today is in French from ListResource" },
            /* This is a starred
             * commment on
             * several lines
             */
	    /* @TMC@ EN MESSAGE {"bye.m", "Goodbye in French from ListResource"}, */
        { "bye.m", "Goodbye in French from ListResource" }
    };
}
