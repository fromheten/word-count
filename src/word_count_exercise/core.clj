(ns word-count-exercise.core
  (:gen-class))

(defn filter-empty [string] (filter #(not (empty? %)) string))
;; This can't be reduced since we sometimes put spaces after word endings.
(defn words [^String text] (filter-empty (re-seq #"[a-zA-Z0-9]*" text)))

(defn word-count [^String text]
  (count (words text)))

(defn word-count-parallel
  "Don't use for strings of < 500 characters. "
  [^String text]
  (reduce + (pmap count (partition-all 500 (words text)))))

(defn line-count
  "Line count is easy to make parallel since determining if something is a new line only uses one character (the \newline one)"
  [text] (reduce (fn [acc curr] (if (= \newline curr)
                                  (inc acc)
                                  acc))
                 1 text))

(defn end-of-word? [character] (condp = character
                                 \space true \. true \! true \? true \- true
                                 false))
(defn characters [text] (->> (map char text)
                             (filter #(not (end-of-word? %)))))

(defn character-count [text] (count (characters text)))

(defn word-count-all [text]
  (str "line:" (line-count text) ", words:" (word-count text) ", chars:" (character-count text)))

(defn word-count-frequencies [text]
  (into [] (frequencies (filter-empty (clojure.string/split text #"\W|_")))))

(defn -main
  "I don't do a whole lot ... yet."
  [& arguments]
  (let [args (set arguments)
        text (slurp (last arguments))]
    (println (cond
               (args "--frequencies") (do (println "--frequencies") (word-count-frequencies text))
               (args "--all") (word-count-all text)
               (args "--parallel") (word-count-parallel text)
               (args "--help") "Welcome to my word counter! Usage: call the program with a file-path (absolute path please) as the last argument. You can optionally have --frequencies, --all or --parallel as first argument. --help with print this message."
               :else (word-count text)))
    (System/exit 0)))
