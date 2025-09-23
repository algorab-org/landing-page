package com.algorabedu.landing

enum SubscriptionResult:
  case Subscribed
  case Unsubscribed
  case AlreadySubscribed
  case InvalidEmail
  case PrivacyNotChecked
  case MiscellaneousError

  def successful = this match
    case Subscribed | Unsubscribed | AlreadySubscribed => true
    case InvalidEmail | MiscellaneousError | PrivacyNotChecked => false

  val translationKey: String = "index.subscription." + this.toString.toLowerCase
