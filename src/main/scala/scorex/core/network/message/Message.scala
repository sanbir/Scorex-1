package scorex.core.network.message

import com.google.common.primitives.{Bytes, Ints}
import scorex.core.crypto.hash.FastCryptographicHash._
import scorex.core.network.ConnectedPeer
import scorex.core.serialization.{ScorexKryoPool, BytesSerializable}

import scala.util.{Success, Try}

case class Message[Content](spec: MessageSpec[Content],
                            input: Either[Array[Byte], Content],
                            source: Option[ConnectedPeer],
                            serializer: ScorexKryoPool) {
  lazy val dataBytes = input match {
    case Left(db) => db
    case Right(d) => serializer.toBytes(d)
  }

  lazy val data: Try[Content] = input match {
    case Left(db) => serializer.fromBytes(db, spec.c)
    case Right(d) => Success(d)
  }
}


object Message {
  type MessageCode = Byte

  val MAGIC = Array(0x12: Byte, 0x34: Byte, 0x56: Byte, 0x78: Byte)

  val MagicLength = MAGIC.length

  val ChecksumLength = 4
}
