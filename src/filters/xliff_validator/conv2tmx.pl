#!/usr/bin/perl

## Here be mapping tables - will want to edit these for any new books/files we deal with
## in the future, I'll stick these in a separate file...


# this mapping table maps from the weird Trados lang codes
# to the SunTrans2 two letter language code

%tradoslang_to_twoletter = (
			    "EN_US" => "en-US",
		            "EN-US" => "en-US",
			    "EN_GB" => "en-GB",
			    "DE-DE" => "de-DE",
                            "FR-FR" => "fr-FR",
			    "KO_KR" => "ko-KR",
			    "IT-IT" => "it-IT",
                            "ES-ES" => "es-ES",
			    "ES-EM" => "es-ES",
			    "RU-RU" => "ru-RU",
			    "PL" => "pl-PL",
			    "ZH-TW" => "zh-TW",
			    "ZH-CN" => "zh-CN",
		            "ZH_CN" => "zh-CN",
			    "JA_01" => "ja-JP",
			    "FR_FR" => "fr-FR",
			    "KO-KR" => "ko-KR",
			    "SV_SE" => "sv-SE"
			   );

if ($#ARGV != 2){
  print "Usage : conv2tmx.pl <srclanguage> <l10n language>  <tradosfile>\n\n";
  print "  Note ! <srclanguage> and <l10n language> need to be the language\n";
  print "  codes found within the trados file you're parsing !\n\n";
  exit 0;
}


$trados_srclang = $ARGV[0];
$trados_l10nlang = $ARGV[1];
$trados_file = $ARGV[2];

$eptas_srclang = $tradoslang_to_twoletter{$trados_srclang};
$eptas_l10nlang = $tradoslang_to_twoletter{$trados_l10nlang};

if ($eptas_srclang eq "" ){
  print "Error - not able to find source $trados_srclang in tradoslang lookup table\n";
  exit 1;
}
if ( $eptas_l10nlang eq ""){
  print "Error - not able to find l10n $trados_l10nlang in tradoslang lookup table\n";
  exit 1;
}


$srcfilename = $tgtfilename = $trados_file;

# Initialize the segment counter.
$segment = 0;

# Open the input file
open TRADOSFILE, "$srcfilename";

# Open the output files (2 here on with spaces at the end of segments, and one without)
open NOSPACEALIFILE, ">" .  $trados_file . "_" . $eptas_l10nlang . ".tmx";

# Write out the ALI file header.    
&writeheader($srcfilename, $tgtfilename, $projectid, $base_team_owner, $booktitle, $createdby, $partno, \*NOSPACEALIFILE);

# Read in the paired translation units and write out stuff
while(<TRADOSFILE>)
  {
    chomp;
    
    # Write out start tag
    if( m@<TrU>@ )
      {
	$segment++;
	print NOSPACEALIFILE "<tu tuid=\"a$segment\">\n";


      }
    
    # Write out the English
    if( m@<Seg L=$trados_srclang>@ )
      {
	s/<Seg L=$trados_srclang>//;
	$_ = &nuke_trados_codes( $_ );
	s/\s+$//;
	
	s/\\rdblquote/\"/g;
	s/\\ldblquote/\"/g;
	
	s/\\tab/\t/g;
	s/\\emdash/-/g;
	s/\\endash/-/g;
	s/\\emspace/ /g;
	s/\\enspace/ /g;
	s/\\bullet/*/g;
	s/\\lquote/\'/g;
	s/\\rquote/\'/g;
	$fixed = &fixtags($_);

	print NOSPACEALIFILE "<tuv xml:lang=\"$eptas_srclang\">";
	print NOSPACEALIFILE "<seg>$fixed</seg>\n";
	print NOSPACEALIFILE "</tuv>\n";
      }
    
    # Write out the French	
    if( m@<Seg L=$trados_l10nlang>@ )
      {
	s/<Seg L=$trados_l10nlang>//;
	$_ = &nuke_trados_codes( $_ );
	# remove ws at end of line
	s/\s+$//;
	# replace nasty RTF character code escapes with something more useful
	s/\\rdblquote/\"/g;
	s/\\ldblquote/\"/g;
	
	s/\\tab/\t/g;
	s/\\emdash/-/g;
	s/\\endash/-/g;
	s/\\emspace/ /g;
	s/\\enspace/ /g;
	s/\\bullet/*/g;
	s/\\lquote/\'/g;
	s/\\rquote/\'/g;
	$fixed = &fixtags($_);

	print NOSPACEALIFILE "<tuv xml:lang=\"$eptas_l10nlang\">";
	print NOSPACEALIFILE "<seg>$fixed</seg>\n";
	

      }
    
    # Write out end tag
    if( m@</TrU>@ )
      {
print ALIFILE  "</tuv>\n";
	print NOSPACEALIFILE "</tuv>\n";
	print NOSPACEALIFILE "</tu>\n";
      }

  }

close TRADOSFILE;

&writefooter( \*NOSPACEALIFILE );

close NOSPACEALIFILE;




#
#  Subroutines 
#
sub main::writeheader
  {
    my $srcfilename     = $_[0];
    my $tgtfilename     = $_[1];
    my $projectid       = $_[2];
    my $base_team_owner = $_[3];
    my $booktitle       = $_[4];
    my $createdby       = $_[5];
    my $partno          = $_[6];

    my $alifile = $_[7];

    print $alifile "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
 print $alifile "<!DOCTYPE tmx SYSTEM \"tmx13.dtd\">";
 print $alifile "<tmx version=\"1.3\">";
 print $alifile "<header adminlang=\"en-US\" srclang=\"en-US\" o-tmf=\"xliff\" segtype=\"sentence\" creationtoolversion=\"Pre FCS\" creationtool=\"SunTrans 2 Trados To TMX Converter\" datatype=\"html\">";
 print $alifile "<prop type=\"SunTrans::DocFile\">$tradosfile</prop>";
 print $alifile "</header>";
 print $alifile "<body>";

}

sub main::writefooter
{
    my $alifile = $_[0];
    print $alifile "</body>";
    print $alifile "</tmx>";
}


sub main::fixtags
  {
    ## this is a *very* crude tag fixing function : want to run over the input string,
    ## and replace tags with <it pos="a1">...</it> tags 

    my $inputstring = $_[0];
    $inputstring =~ s/&/&amp;/g;
    
    # this can trip us up..
    $doubleOpen = $inputstring =~ s/>>/&gt;&gt;/g;
    $doubleClose = $inputstring =~ s/<</&lt;&lt;/g;
    
    $inputstring =~ s/</&lt;/g;
    $inputstring =~ s/>/&gt;/g;

      # special handling for comments
    $commentCount = $inputstring =~ s/&lt;!--(.*)--&gt;/<it pos="begin"><!--\1--><\/it>/g;

    # special handling for <%JSP tags%> - turn these back into normal text for now
    $jspCount = $inputstring =~ s/&lt;%([=a-zA-Z0-9]*)%&gt;/<%\1%>/g;    
	
    # special handling for <jato:text /> namespaced tags
    $nstagCount = $inputstring =~ s/&lt;([a-zA-Z0-9]*):([0-9a-zA-Z ="\$]*)\/&gt;/<\1:\2\/>/g;

    $preit = $inputstring;

    if ($commentCount == 0){
      # count the number of opening and closing <it> substitutions
      $openIt = $inputstring =~ s/&lt;/<it pos="begin">&lt;/g;
      $closeIt = $inputstring =~ s@&gt;@&gt;</it>@g;

      if ($openIt != $closeIt || $doubleOpen > 0 || doubleClose > 0){      
        # print STDERR "Opened or closed more IT tags - invalid XML results ! for $inputstring\n";
        # escape each individual > and < in their own it tag.
        $preit =~ s/&lt;/<it pos="begin">&lt;<\/it>/g;
        $preit =~ s/&gt;/<it pos="begin">&gt;<\/it>/g;
        $inputstring = $preit;
      }


    } else {
      # print STDERR "Comment string found in there !\n";
      $inputstring =~ s/<it pos="begin"><!--(.*)--><\/it>/<it pos="begin">&lt;!--\1--&gt;<\/it>/g;
    }

    # fix jsp tags - this isn't perfect, we don't know if the jsp tags are inside other
    # <it> tags already - so converting them to plaintext is all we can do.
    # ideally, we'd detect jsp tags that are already in it tags, and leave them alone
    # but jsp tags that were not already in it tags would be properly wrapped in <it>
    $inputstring =~ s/<%([=a-zA-Z0-9]*)%>/&lt;%\1%&gt;/g;

    # same here for namespaces... not good.
    $inputstring =~ s/<([a-zA-Z0-9]*):([0-9a-zA-Z ="\$]*)\/>/&lt;\1:\2\/&gt;/g;

    $fixedtags = $inputstring;

}


sub main::nuke_trados_codes
{
    my $line = $_;
    

    $old = $line;

    $line =~ s/{(\\[^ \t\n\\<&%;.,!?\[\]()-]+)+ //g;
    # also want to get rid of those nasty trados tags...
    $line =~ s/\<:.*\>//g;

    # some trados files look like :
    # Solaris Documentation CD {\cs6\f1\cf6\lang1024 <br>}Important Information
    # which gets converted to :
    # Solaris Documentation CD  <br>Important Information
    # Some however look like :
    # Solaris Documentation CD {\cs6\f1\cf6\lang1024<br>}Important Information
    # so we just check to see if that's there, and otherwise change it with the
    # below regexp with the space

    if ($line eq $old){
	$line =~ s/{(\\[^ \t\n\\<&%;.,!?\[\]()-]+)+//g;
    }

		  

    $line =~ s/([^\\])}/$1/g;
    $line =~ s/\\{/{/g;
    $line =~ s/\\}/}/g;

    # remove broken trados tags like
    # <:SEGS> <#NUM>..etc
    #
    my @tags = ( "<:.*?>","<#.*?>" ); my $re = "";
    for $re (@tags) {
        while($line =~ /$re/) {
            $line =~ s/$re//g;
        }
    }

    return $line;
}

