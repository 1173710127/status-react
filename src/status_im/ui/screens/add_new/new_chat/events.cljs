(ns status-im.ui.screens.add-new.new-chat.events
  (:require [clojure.string :as string]
            [re-frame.core :as re-frame]
            [status-im.ethereum.core :as ethereum]
            [status-im.ethereum.ens :as ens]
            [status-im.ethereum.resolver :as resolver]
            [status-im.ui.screens.add-new.new-chat.db :as db]
            [status-im.utils.handlers :as handlers]
            [status-im.ethereum.stateofus :as stateofus]))

(re-frame/reg-fx
 :resolve-public-key
 (fn [{:keys [registry ens-name cb]}]
   (resolver/pubkey registry ens-name cb)))

(handlers/register-handler-fx
 :new-chat/set-new-identity
 (fn [{db :db} [_ new-identity new-ens-name]]
   (let [is-public-key? (and (string? new-identity)
                             (string/starts-with? new-identity "0x"))]
     (merge {:db (assoc db
                        :contacts/new-identity {:public-key new-identity :ens-name new-ens-name}
                        :contacts/new-identity-error (db/validate-pub-key db new-identity))}
            (when (and (not is-public-key?)
                       (ens/valid-eth-name-prefix? new-identity))
              (let [chain    (ethereum/chain-keyword db)
                    ens-name (string/lower-case
                              (if (ens/is-valid-eth-name? new-identity)
                                new-identity
                                (stateofus/subdomain new-identity)))]
                {:resolve-public-key {:registry (get ens/ens-registries chain)
                                      :ens-name ens-name
                                      :cb       #(re-frame/dispatch [:new-chat/set-new-identity % ens-name])}}))))))
