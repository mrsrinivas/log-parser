import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSpec, GivenWhenThen}

@RunWith(classOf[JUnitRunner]) // For gradle to pick up this via JUnit
class LogParserSpec extends FunSpec with BeforeAndAfter with GivenWhenThen {

  var lines: Array[String] = _
  var parser: LogParser = _

  before {
    parser = new LogParser
    lines =
      s"""com.apple.watchlistd/1.0 iOS/12.1.4 model/iPad6,11 hwp/s8003 build/16D57 (5; dt:172)
         |com.apple.watchlistd/1.0 iOS/11.4 model/iPad6,4 hwp/s8001 build/15F79 (5; dt:127)
         |com.apple.watchlistd/1.0 iOS/13.0 model/iPhone11,6 hwp/t8020 build/17A3457o (6; dt:189) AMS/(null)
         |com.apple.watchlistd/1.0 iOS/12.2 model/iPhone8,4 hwp/s8003 build/16E227 (6; dt:129)
         |iTunes/12.7.4 (Macintosh; OS X 10.13.2) AppleWebKit/604.4.7.1.3 hwp/t8002 (dt:1)
         |iTunes/12.7.4 (Macintosh; OS X 10.13.4) AppleWebKit/605.1.33.1.4 hwp/t8002 (dt:1)
         |watchlistd/1.0 iOS/12.1.1 AppleTV/12.1.1 model/AppleTV6,2 hwp/t8011 build/16K5044a (3; dt:163)
         |watchlistd/1.0 iOS/12.0 AppleTV/12.0 model/AppleTV6,2 hwp/t8011 build/16J364 (3; dt:163)"""
        .stripMargin.split("\\n")
  }

  //watchlistd/1.0 iOS/12.1.1 AppleTV/12.1.1 model/AppleTV6,2 hwp/t8011 build/16K5044a (3; dt:163)
  describe("Testing the for a AppleTV record ...") {

    it("the fields should be parsed correctly") {
      Given("the AppleTV access log record")
      val appleTVRecord = lines.filter(_.contains("AppleTV"))(0)
      val record: LogRecord = parser.parse(appleTVRecord)

      Then("device type should not be other")
      assert(record.deviceType != "other")

      And("device should be AppleTV")
      assert(record.deviceType == "AppleTV")
      assert(parser.getDeviceType(appleTVRecord) == "AppleTV")

      And("OS major version should be 12")
      assert(record.osMajor == "12")
      assert(parser.getOSMajor(appleTVRecord) == "12")

      And("OS minor version should be 12.1.1")
      assert(record.osMinor == "12.1.1")
      assert(parser.getOSMinor(appleTVRecord) == "12.1.1")
    }
  }

  // com.apple.watchlistd/1.0 iOS/13.0 model/iPhone11,6 hwp/t8020 build/17A3457o (6; dt:189) AMS/(null)
  describe("Testing the for a mobile device record ...") {

    it("the fields should be parsed correctly") {
      Given("the mobile device access log record")
      val mobileDeviceRecord = lines.filter(_.contains("iPhone11"))(0)
      val record: LogRecord = parser.parse(mobileDeviceRecord)

      Then("device type should not be other")
      assert(record.deviceType != "other")

      And("device should be iPhone")
      assert(record.deviceType == "iPhone")
      assert(parser.getDeviceType(mobileDeviceRecord) == "iPhone")

      And("OS major version should be 13")
      assert(record.osMajor == "13")
      assert(parser.getOSMajor(mobileDeviceRecord) == "13")

      And("OS minor version should be 13.0")
      assert(record.osMinor == "13.0")
      assert(parser.getOSMinor(mobileDeviceRecord) == "13.0")
    }
  }


  //iTunes/12.7.4 (Macintosh; OS X 10.13.2) AppleWebKit/604.4.7.1.3 hwp/t8002 (dt:1)
  describe("Testing the for a mac device record ...") {

    it("the fields should be parsed correctly") {
      Given("the mac device access log record")
      val macDeviceRecord = lines.filter(_.contains("Macintosh"))(0)
      val record: LogRecord = parser.parse(macDeviceRecord)

      Then("device type should not be other")
      assert(record.deviceType != "other")

      And("device should be Macintosh")
      assert(record.deviceType == "Macintosh")
      assert(parser.getDeviceType(macDeviceRecord) == "Macintosh")

      And("OS major version should be 10")
      assert(record.osMajor == "10")
      assert(parser.getOSMajor(macDeviceRecord) == "10")

      And("OS minor version should be 10.13.2")
      assert(record.osMinor == "10.13.2")
      assert(parser.getOSMinor(macDeviceRecord) == "10.13.2")
    }

  }

  describe("Testing the for an empty line ...") {

    it("the fields should be parsed correctly") {
      Given("the mac device access log record")
      val emptyRecord = ""
      val record: LogRecord = parser.parse(emptyRecord)

      Then("device type should be other")
      assert(record.deviceType == "other")
      assert(parser.getDeviceType(emptyRecord) == "other")

      And("OS major version should be empty")
      assert(record.osMajor.isEmpty)
      assert(parser.getOSMajor(emptyRecord).isEmpty)

      And("OS minor version should be empty")
      assert(record.osMinor.isEmpty)
      assert(parser.getOSMinor(emptyRecord).isEmpty)
    }
  }

}
