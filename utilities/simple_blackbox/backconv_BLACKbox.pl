#!/usr/bin/perl

# die if we have bad arguments or files does not exists
if($#ARGV!=1 || !-e $ARGV[0] || !-e $ARGV[1]) {
    die "backconv_BLACKbox golden_result.zip test_result.zip\n";
}

@content = `unzip -l $ARGV[0]`;
# walk through the zip content, unzip non xlz files and compare content with unix diff
for (@content) {
    if(/\.\/.*\/(.*)$/ && !/\.xlz$/) {
        `unzip -qp $ARGV[0] *$1 > /tmp/golden.tst`;
        `unzip -qp $ARGV[1] *$1 > /tmp/test.tst`;
        @diff = `diff -c /tmp/golden.tst /tmp/test.tst`;
        if($#diff==-1) {
            print STDOUT "OK $1\n";
        } else {
            print STDERR "KO $1\n";
            print STDERR @diff;
        }
    }
}
