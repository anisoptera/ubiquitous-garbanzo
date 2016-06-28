(ns screeps.creep
  (:refer-clojure :exclude [name])
  (:use [screeps.utils :only [jsx->clj]]
        [screeps.memory :only [*memory*]]))

(defn id
  [c]
  (.-id c))

(defn body
  [c]
  (jsx->clj (.-body c)))

(defn name
  [c]
  (.-name c))

(defn room
  [c]
  (.-room c))

(defn move
  [c direction]
  (.move c direction))

(defn move-to
  [c target]
  (.moveTo c target))

(defn build
  [c t]
  (.build c t))

(defn repair
  [c t]
  (.repair c t))

(defn harvest
  [c t]
  (.harvest c t))

(defn pickup
  [c t]
  (.pickup c t))

(defn spawning?
  [c]
  (.-spawning c))

(defn energy
  [c]
  (aget c "carry" "energy"))

(defn energy-capacity
  [c]
  (aget c "carryCapacity"))

(defn transfer-energy
  [c t]
  (.transfer c t js/RESOURCE_ENERGY nil))

(defn drop-energy
  [c]
  (.drop c js/RESOURCE_ENERGY))

(defn upgrade-controller
  [c ctrl]
  (.upgradeController c ctrl))

(defn claim-controller
  [c ctrl]
  (.claimController c ctrl))

(defn ttl
  [c]
  (.-ticksToLive c))

(defn memory
  [c]
  (get-in @*memory* ["creeps" (name c)] {}))

(defn memory!
  [c m]
  (swap! *memory* #(assoc-in % ["creeps" (name c)] m)))

