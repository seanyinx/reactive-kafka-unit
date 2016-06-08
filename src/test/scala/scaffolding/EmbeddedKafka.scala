package scaffolding

import com.github.charithe.kafka.KafkaJunitRule

case class EmbeddedKafka(port: Int = -1) extends KafkaJunitRule(port) {
  def start() = before()

  def stop() = after()
}
