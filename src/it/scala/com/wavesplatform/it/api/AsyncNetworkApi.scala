package com.wavesplatform.it.api

import java.net.InetSocketAddress

import com.wavesplatform.network.client.NetworkSender
import com.wavesplatform.network.RawBytes

import scala.concurrent.Future

object AsyncNetworkApi {

  implicit class NodeExt2(n: Node) {

    import scala.concurrent.ExecutionContext.Implicits.global

    def nonce: Long = System.currentTimeMillis()

    def sendByNetwork(message: RawBytes*): Future[Unit] = {
      val sender = new NetworkSender(n.chainId, n.nodeName, nonce)
      sender.connect(new InetSocketAddress(n.restAddress, n.networkPort)).map { ch =>
        if (ch.isActive) sender.send(ch, message: _*).map(_ => sender.close()) else sender.close()
      }
    }
  }
}
