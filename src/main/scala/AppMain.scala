

object AppMain {

  def main(args: Array[String]): Unit = {
    val log: Array[String] =
      s"""com.apple.watchlistd/1.0 iOS/12.1.4 model/iPad6,11 hwp/s8003 build/16D57 (5; dt:172)
         |com.apple.watchlistd/1.0 iOS/11.4 model/iPad6,4 hwp/s8001 build/15F79 (5; dt:127)
         |com.apple.watchlistd/1.0 iOS/13.0 model/iPhone11,6 hwp/t8020 build/17A3457o (6; dt:189) AMS/(null)
         |com.apple.watchlistd/1.0 iOS/12.2 model/iPhone8,4 hwp/s8003 build/16E227 (6; dt:129)
         |iTunes/12.7.4 (Macintosh; OS X 10.13.2) AppleWebKit/604.4.7.1.3 hwp/t8002 (dt:1)
         |iTunes/12.7.4 (Macintosh; OS X 10.13.4) AppleWebKit/605.1.33.1.4 hwp/t8002 (dt:1)
         |watchlistd/1.0 iOS/12.1.1 AppleTV/12.1.1 model/AppleTV6,2 hwp/t8011 build/16K5044a (3; dt:163)
         |watchlistd/1.0 iOS/12.0 AppleTV/12.0 model/AppleTV6,2 hwp/t8011 build/16J364 (3; dt:163)"""
        .stripMargin.split("\\n")

    val parser = new LogParser

    log //.filter(line => line.startsWith("watchlistd"))
      .map(parser.parse(_))
      .foreach(println)
  }
}