
/**
  * This is used to parse the log line and extract necessary attributes
  */
class LogParser {

  // This can be built with come common expressions for OS version and device type variables and so on
  private val regexAppleTv = "watchlistd/\\d+[.\\d+]{1,} iOS/(\\d+[.\\d+]{1,}) (AppleTV)/\\d+[.\\d+]{1,} model/.*".r
  private val regexMac = "iTunes/\\d+[.\\d+]{1,} \\(([A-Za-z]+); OS X (\\d+[.\\d+]{1,})\\) AppleWebKit/\\d+[.\\d+]{1,} hwp/.*".r
  private val regexMobile = "com.apple.watchlistd/\\d+[.\\d+]{1,} iOS/(\\d+[.\\d+]{1,}) model/([a-zA-Z]+)[\\d+]{1,}.*".r

  def parse(line: String): LogRecord = {
    line match {
      case regexAppleTv(osFullVersion, deviceType) =>
        LogRecord(deviceType, osFullVersion.split("\\.")(0), osFullVersion)
      case regexMac(deviceType, osFullVersion) =>
        LogRecord(deviceType, osFullVersion.split("\\.")(0), osFullVersion)
      case regexMobile(osFullVersion, deviceType) =>
        LogRecord(deviceType, osFullVersion.split("\\.")(0), osFullVersion)

      //If there is no default handling, return should be `Option[LogRecord]`
      case _ => LogRecord("other", "", "")
    }
  }

  def getDeviceType(line: String): String = {
    parse(line).deviceType
  }

  def getOSMajor(line: String): String = {
    parse(line).osMajor
  }

  def getOSMinor(line: String): String = {
    parse(line).osMinor
  }
}