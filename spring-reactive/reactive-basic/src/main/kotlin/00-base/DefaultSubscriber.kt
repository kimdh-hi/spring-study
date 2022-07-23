package `00-base`

import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription


class DefaultSubscriber(
  val name: String? = null
): Subscriber<Any> {

  override fun onSubscribe(subscription: Subscription) {
    subscription.request(Long.MAX_VALUE)
  }

  override fun onNext(item: Any) {
    println("$name - Received: $item")
  }

  override fun onError(throwable: Throwable) {
    println("$name - Error: ${throwable.message}")
  }

  override fun onComplete() {
    println("$name - Complete")
  }
}