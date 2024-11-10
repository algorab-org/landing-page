package com.algorabedu.landing

enum SubscriptionResult:
  case Subscribed
  case Unsubscribed
  case AlreadySubscribed
  case InvalidEmail
  case MiscellaneousError

  def successful = this match
    case Subscribed | Unsubscribed | AlreadySubscribed => true
    case InvalidEmail | MiscellaneousError => false

  val translationKey: String = "index.subscription." + this.toString.toLowerCase
  