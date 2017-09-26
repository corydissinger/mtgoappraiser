Welcome to mtgoappraiser!
=========================

# Running on Windows

Assuming `mvn clean install` does not hiccup, you will get ${project.dir}/target/mtgo-appraiser-X.Y.Z-.jar. Modify application.properties to your liking. It must be in the same directory as the jar that is produced. It is highly recommended you use a directory _OTHER THAN_ the Maven generated /target nonsense.

Run start.bat from the command line.

# A few notes...

This thing is scraping website data. We will attempt to cache data for 24 hours since most market movements don't happen faster than that (and it's 'polite' to the IT dummies who think they're wasting time/money dealing with requests. Anyone hear of a proxy? Load balancer? If you're a web-based administrator of any kind and you think this statement is offensive, find a new career.). If you're trying to follow a spike you can delete the cache folder.

Some people are meanies and try to out-dumb scraping by having 'obfuscation CSS'. If they do this might stop working. This is easily countered with a quick and dirty OCR hack that maps the 'secret hashed CSS Classes' and their background-position to the corresponding x/y coordination of the background-image:url.

If my.really.smrt.subdomain.com understood how to interrogate HTTP Headers more effectively, they might not have wasted their time on such a trivially circumvented solution. Oh well, some people want to pay money to smart sounding people for the sake of lightning cash on fire.

`<html>
  <head>
  ... irrelevant requests and/or tragically terrible inline <script> or <style> tags ...
    <style>
      .PoYqhC {		background-image:url(//my.really.smrt.subdomain.com/price_icons?id=someComicallyHashedString);	}	
      .NsfUve {		width:7px;		float:left;		height:14px;	}
      .jpgcMq {background-position:-56px -2px;}
    .jpgcMq2 {background-position:-56px 21px;}
    .iJjfsm {background-position:-35px -2px;}
    .iJjfsm2 {background-position:-35px 21px;}
    .ybhoae {background-position:-42px -2px;}
    .ybhoae2 {background-position:-42px 21px;}
    .mquLuo {background-position:-28px -2px;}
    .mquLuo2 {background-position:-28px 21px;}
    .vnmuqq {background-position:-14px -2px;}
    .vnmuqq2 {background-position:-14px 21px;}
    .ycRrSo {background-position:-21px -2px;}
    .ycRrSo2 {background-position:-21px 21px;}
    .kKabhx {background-position:-66px -2px;}
    .kKabhx2 {background-position:-66px 21px;}
    .zakpya {background-position:-7px -2px;}
    .zakpya2 {background-position:-7px 21px;}
    .coYlfk {background-position:0px -2px;}
    .coYlfk2 {background-position:0px 21px;}
    .fmpBwS {background-position:-63px -2px;width:3px; }
    .fmpBwS2 {background-position:-63px 21px;width:3px; }
    .gugQrW {background-position:-49px -2px;}
    .gugQrW2 {background-position:-49px 21px;}
    </style>  
  ... irrelevant requests and/or tragically terrible inline <script> or <style> tags ...    
  </head>
  <body>
    ... arbitrary nonsense
    <div class="coYlfk2 NsfUve PoYqhC">&nbsp;</div>
    ... continued nonsense
  </body>
</html>`

# About your appraised collection CSV

The MTGO traders column pulls data from here - http://www.mtgotraders.com/hotlist/#/
You have to go to a special bot to sell at their price. It's generally going to be the best price you can get.
