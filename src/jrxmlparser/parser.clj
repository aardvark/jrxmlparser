(ns jrxmlparser.parser)
(require '[clojure.xml :as xml])

(defn get-content
  "Get all top level fields from xml"
  [path]
  (let [xml (xml/parse path)]
    (select-keys xml [:content])))

(defn get
  "Filter collection by key and get contents of this key"
  [parsed-xml type]
  (let [key (keyword type)]
    (key (select-keys parsed-xml [(keyword type)]))))

(defn get-child-by-type
  "Get child from content by type"
  [content type]
  (let [key-type (keyword type)]
    (filter
     #(= key-type (:tag (select-keys % [:tag])))
     (:content content))
  ))

 (filter #(= :field (:tag (select-keys % [:tag])))(:content (get-fields "resources/MasterReport.jrxml")))

