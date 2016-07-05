(ns screeps.creep
  (:refer-clojure :exclude [name])
  (:require [screeps.game :as game]
            [screeps.position :as pos])
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

(defn fatigue
  [c]
  (.-fatigue c))

(defn build
  [c t]
  (.build c t))

(defn repair
  [c t]
  (.repair c (clj->js t)))

(defn harvest
  [c t]
  (.harvest c t))

(defn pickup
  [c t]
  (.pickup c t))

(defn spawning?
  [c]
  (.-spawning c))

(defn carry
  [c]
  (jsx->clj (aget c "carry")))

(defn energy
  [c]
  (aget c "carry" "energy"))

(defn cargo-amount
  [c]
  (reduce + (vals (carry c))))

(defn energy-capacity
  [c]
  (aget c "carryCapacity"))

(defn transfer-everything
  [c t]
  (doseq [i (carry c)]
    (.transfer c t (name (first i)) nil)))

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
  (or (get-in @*memory* ["creeps" (name c)] {})
      {}))

(defn memory!
  [c m]
  (swap! *memory* #(assoc-in % ["creeps" (name c)] m)))

(def path-freshness 20)

(defn move-by-path
  [c path]
  (.moveByPath c (clj->js path)))

(defn move-to
  [c target]
  (if (= 0 (fatigue c))
    (let [m (memory c)
          target-pos (pos/position target)
          [stamp [x y] path] (get m "path" [])]
      (if (and (not (nil? path))
               (pos/eq? target-pos x y)
               (> path-freshness (- (game/time) stamp)))
        (move-by-path c path)
        (let [path (pos/find-path-to c target)]
          (memory! c (assoc m "path" [(game/time) (pos/get-xy target) path]))
          (let [result (move-by-path c path)]
            (if-not (or (= 0 result)
                        (= js/ERR_TIRED result))
              (memory! m (dissoc m "path")))
            result))))
    js/ERR_TIRED))
