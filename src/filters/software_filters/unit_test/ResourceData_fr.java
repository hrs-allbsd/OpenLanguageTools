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
