(ns Problem5
  (:require
    [clojure.string :as str]
    [clojure.set :as sets]
    ))

(def input (str/split-lines (slurp "Problem5.txt")))

(def line-regex #"move (\d+) from (\d+) to (\d+)")
(defn filter-empty-lines [line] (not (str/blank? (first line))))
(defn not-empty-char? [c] (not (str/blank? (str c))))

(defn split-input [input]
  (->>
    input
    (partition-by str/blank?)
    (filter filter-empty-lines)
    )
  )

(defn index-to-str-location [index] (- (* 4 index) 3))


(defn build-cargo [top]
  (let
    [
     first (str/replace (str/trim (first top)) #"\s+" " ")
     container-count (count (str/split first #" "))
     list-needs-pivot-applied (map (fn [arg] (map (fn [index] (nth arg index "")) (map index-to-str-location (range 1 (+ 1 container-count))))) (rest top))

     pivoted (map
               (fn [index] (map (fn [arg] (nth arg index)) list-needs-pivot-applied))
               (range 0 container-count))

     ]
    {
     :count container-count
     :cargo (vec (map (fn [row] (filter not-empty-char? row)) pivoted))
     }
    ))

(defn parse-line [line]
  (let [matched (re-matches line-regex line)]
    {
     :count (Integer/parseInt (nth matched 1))
     :from  (- (Integer/parseInt (nth matched 2)) 1)
     :to    (- (Integer/parseInt (nth matched 3)) 1)
     }
    ))

(defn apply-instruction-once
  [instruction containers]
  (let [
        a (:count instruction)
        to-remove-from (nth containers (:from instruction))
        remaining (reverse (nthrest (reverse to-remove-from) a))
        last-from (take-last a to-remove-from)
        to (nth containers (:to instruction))
        ]
    (assoc containers
      (:from instruction) remaining
      (:to instruction) (concat to last-from)
      )
    ))

(defn apply-instruction [instruction containers]
  (if (= (:count instruction) 0)
    containers
    (apply-instruction
      (assoc instruction :count (- (:count instruction) 1))
      (apply-instruction-once (assoc instruction :count 1) containers)
      ))
  )

(defn run [apply-function instructions data]
  (if (empty? instructions)
    data
    (run apply-function (rest instructions) (apply-function (first instructions) data))
    ))


(let [
      input-split (split-input input)
      top (reverse (first input-split))

      cargo (:cargo (build-cargo top))
      instructions (map parse-line (flatten (rest input-split)))
      ]
  (println cargo)
  (println (map last (run apply-instruction instructions cargo)))
  (println (map last (run apply-instruction-once instructions cargo)))
  )

