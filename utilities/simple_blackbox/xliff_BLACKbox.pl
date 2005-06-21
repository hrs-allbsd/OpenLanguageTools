#!/usr/bin/perl

# die if we have bad arguments or files does not exists
if($#ARGV!=1 || !-e $ARGV[0] || !-e $ARGV[1]) {
    die "xliff_BLACKbox golden_result.zip test_result.zip\n";
}

@content = `unzip -l $ARGV[0] *.xlz`;
# walk through the zip content, unzip xlz files and compare content with unix diff
for (@content) {
    if(/.*\/(.*\.xlz)/) {
        `unzip -qp $ARGV[0] *$1 | gzip -dq - > /tmp/golden.xml`;
        `unzip -qp $ARGV[1] *$1 | gzip -dq - > /tmp/test.xml`;
        @diff = `diff -c /tmp/golden.xml /tmp/test.xml`;
        if($#diff==-1) {
            print STDOUT "OK $1\n";
        } else {
            print STDERR "KO $1\n";
            print STDERR @diff;
        }
    }
}
