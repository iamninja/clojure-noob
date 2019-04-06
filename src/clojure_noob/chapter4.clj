;; Chapter 4

(ns clojure-noob.chapter4)

(defn titleize
  [topic]
  (str topic " for the Brave and True"))

(map titleize ["Hamsters" "Ragnarok"])

(map titleize '("Empathy" "Decorating"))

(map titleize #{"Elbows" "Soap Carving"})

(map #(titleize (second %)) {:uncomfortable-thing "Winking"})


(map inc [1 2 3])

(map str ["a" "b" "c"] ["A" "B" "C"])

(def human-consumption   [8.1 7.3 6.6 5.0])
(def critter-consumption [0.0 0.2 0.3 1.1])
(defn unify-diet-data
  [human critter]
  {:human human
   :critter critter})

(map unify-diet-data human-consumption critter-consumption)

(def sum #(reduce + %))
(def avg #(/ (sum %) (count %)))

(defn stats
  [numbers]
  (map #(% numbers) [sum count avg]))

(stats [3 4 10])
(stats [3 4 62 153 456 1])

;; Keywords can be used as functions
(def identities
  [{:alias "Batman"       :real "Bruce Wayne"}
   {:alias "Spider-Man"   :real "Peter Parker"}
   {:alias "Santa"        :real "Your mama"}
   {:alias "Easter Bunny" :real "Your papa"}])

(map :real identities)

(reduce (fn [new-map [key val]]
          (assoc new-map key (inc val)))
        {}
        {:max 30 :min 10})

(reduce (fn [new-map [key val]]
          (if (> val 4)
            (assoc new-map key val)
            new-map))
        {}
        {:human 4.1 :critter 3.9})

(defn red-map
  [fun collection]
  (reduce #(conj %1 (fun %2)) [] collection))

(defn red-map2
  [fun collection]
  (reduce (fn [result item]
            (conj result (fun item)))
          [] collection))

(red-map inc [1 2 3])
(red-map2 inc [1 2 3])

;; Retrun the first n from a sequence
(take 3 [1 2 3 4 5])

;; Drops the first n from a seq
(drop 3 [1 2 3 4 5])

(def food-journal
  [{:month 1 :day 1 :human 5.3 :critter 2.3}
   {:month 1 :day 2 :human 5.1 :critter 2.0}
   {:month 2 :day 1 :human 4.9 :critter 2.1}
   {:month 2 :day 2 :human 5.0 :critter 2.5}
   {:month 3 :day 1 :human 4.2 :critter 3.3}
   {:month 3 :day 2 :human 4.0 :critter 3.8}
   {:month 4 :day 1 :human 3.7 :critter 3.9}
   {:month 4 :day 2 :human 3.7 :critter 3.6}])

;; Take/drop-while do the same with respect to a predicate
(take-while #(< (:month %) 3) food-journal)

(drop-while #(< (:month %) 3) food-journal)

(take-while #(< (:month %) 4)
            (drop-while #(< (:month %) 2) food-journal))

;; Filter reads all data. If the data are sorted
;; then drop/take are more efficient.
(filter #(< (:human %) 5) food-journal)
(filter #(< (:month %) 3) food-journal)

;; some returns the first "truthy" result of the function given, else nil
;; predicate?
(some #(> (:critter %) 5) food-journal)
(some #(> (:critter %) 2) food-journal)

(some #(and (> (:critter %) 3) %) food-journal)

;; sort
(sort [3 1 2])
(sort-by count ["asdf" "sdf" "sasdas"])

;; concat seqs
(concat [1 2] [3 4] [4 5])

;; Lazy seq
(def vampire-database
  {0 {:makes-blood-puns? false, :has-pulse? true :name "McFishwich"}
   1 {:makes-blood-puns? false, :has-pulse? true :name "McMackson"}
   2 {:makes-blood-puns? true, :has-pulse? false :name "Damon Salvatore"}
   3 {:makes-blood-puns? true, :has-pulse? true :name "Mickey Mouse"}})

(defn vampire-related-details
  [social-security-number]
  (Thread/sleep 1000)
  (get vampire-database social-security-number))

(defn vampire?
  [record]
  (and (:makes-blood-puns? record)
       (not (:has-pulse? record))
       record))

(defn identify-vampire
  [social-security-numbers]
  (first (filter vampire?
                 (map vampire-related-details social-security-numbers))))

(time (vampire-related-details 0))

(time (def mapped-details (map vampire-related-details (range 0 1000000))))

(time (first mapped-details))

(time (identify-vampire (range 0 1000000)))

;; Infinite seqs
;; (repeat "na") is ("na" "na" ...)
(concat (take 8 (repeat "na")) ["Batman!"]) 

;; repeatedly calls the fun to generate each element
(take 3 (repeatedly (fn [] (rand-int 10))))

(defn even-numbers
  ([] (even-numbers 0))
  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))

(take 10 (even-numbers))

(cons 0 '(2 4 6))

;; Collection abstraction
(empty? [])

(empty? ["no!"])

;; into
;; converting seq output
(map identity {:sunlight-reaction "Glitter!"})
(into {} (map identity {:sunlight-reaction "Glitter!"}))

(map identity [:garlic :sesame-oil :fried-eggs])
(into [] (map identity [:garlic :sesame-oil :fried-eggs]))

(map identity [:garlic-clove :garlic-clove])
(into #{} (map identity [:garlic-clove :garlic-clove]))

;; adding elements to collections
(into {:favorite-emotion "gloomy"} [[:sunlight-reaction "Glitter!"]])
(into ["cherry"] '("pine" "spruce"))
(into {:favorite-animal "kitty"} {:least-favorite-smell "dog"
                                  :relationship-with-teenager "creepy"})
;; conj
(conj [0] [1])
(into [0] [1])
(conj [0] 1)
(conj [0] 1 2 3 4 5 6 7 8 9)
(conj {:time "midnight"} [:place "ye old cemetarium"])

;; conj using into
(defn my-conj
  [target & additions]
  (into target additions))
(my-conj [0] 1 2 3 4)

;; Function Functions
;; apply, explodes seqable into a fun
(max 0 1 2)
(max [0 1 2])
(apply max [0 1 2])

;; into using conj
(defn my-into
  [target additions]
  (apply conj target additions))

(my-into [0] [1 2 3])
