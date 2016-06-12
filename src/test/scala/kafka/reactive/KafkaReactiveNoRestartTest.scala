package kafka.reactive

import akka.actor.ActorSystem
import akka.kafka.scaladsl._
import akka.kafka.{ConsumerSettings, ProducerSettings}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import org.apache.curator.test.InstanceSpec
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.scalatest._
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}

import scala.concurrent.Await
import scala.concurrent.duration._

class KafkaReactiveNoRestartTest extends WordSpec with BeforeAndAfter with BeforeAndAfterAll with Matchers {
  implicit val config = EmbeddedKafkaConfig(kafkaPort = port)

  implicit val system = ActorSystem("KafkaReactiveTest")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  lazy val port = InstanceSpec.getRandomPort

  override protected def beforeAll(): Unit = {
    EmbeddedKafka.start()
  }

  override protected def afterAll(): Unit = {
    EmbeddedKafka.stop()
    system.terminate()
  }

  "Kafka" should {
    "send and receive a single message" in {
      val message: String = "some message"

      Source.single(message)
        .map(elem => new ProducerRecord[String, String]("topic1", elem))
        .to(Producer.plainSink(localProducerSettingsOn(port)))
        .run()

      val future = Consumer.plainSource(localConsumerSettingsOn(port, Set("topic1")))
        .runWith(Sink.head)
        .map(record => new String(record.value()))

      Await.result(future, 3.seconds) should ===(message)
    }

    "send and receive multiple messages" in {
      Source(1 to 3)
        .map("message" + _.toString)
        .map(elem => new ProducerRecord[String, String]("topic2", elem))
        .to(Producer.plainSink(localProducerSettingsOn(port)))
        .run()

      val future = Consumer.plainSource(localConsumerSettingsOn(port, Set("topic2")))
        .map(record => new String(record.value()))
        .take(3)
        .runFold(Array.empty[String])((arr, s) => arr :+ s)

      Await.result(future, 3.seconds) should contain allOf("message1", "message2", "message3")
    }
  }

  private def localConsumerSettingsOn(port: Int, topic: Set[String]): ConsumerSettings[String, String] = {
    ConsumerSettings(system, new StringDeserializer, new StringDeserializer, topic)
      .withBootstrapServers(s"127.0.0.1:$port")
      .withGroupId("group1")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
  }

  private def localProducerSettingsOn(port: Int): ProducerSettings[String, String] = {
    ProducerSettings(system, new StringSerializer, new StringSerializer)
      .withBootstrapServers(s"127.0.0.1:$port")
  }
}
