(ns status-im.transport.impl.send
  (:require [status-im.group-chats.core :as group-chats]
            [status-im.utils.fx :as fx]
            [status-im.pairing.core :as pairing]
            [status-im.multiaccounts.update.core :as multiaccounts.update]
            [status-im.transport.db :as transport.db]
            [status-im.transport.message.pairing :as transport.pairing]
            [status-im.transport.message.contact :as transport.contact]
            [status-im.transport.message.protocol :as protocol]))

(extend-type transport.pairing/PairInstallation
  protocol/StatusMessage
  (send [this _ cofx]
    (pairing/send-pair-installation cofx this)))

(extend-type transport.pairing/SyncInstallation
  protocol/StatusMessage
  (send [this _ cofx]
    (pairing/send-sync-installation cofx this)))
