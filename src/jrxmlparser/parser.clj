(ns jrxmlparser.parser)
(require '[clojure.data.xml :as xml]
         '[clojure.zip :as zip]
         '[clojure.pprint :as p]
         '[clojure.data.zip.xml :as xz])



(defn get-content
  "Get all top level fields from xml"
  [path]
  (let [xml (xml/parse path)]
    (select-keys xml [:content])))

(-> "resources/MasterReport.jrxml" clojure.java.io/input-stream xml/parse)

(defn get-child-by-type
  "Get child from content by type"
  [content type]
  (let [key-type (keyword type)]
    (filter
     #(= key-type (:tag (select-keys % [:tag])))
     (:content content))
  ))

(defn- select-keys-in
  "select keys inside collection"
  [keys xs]
  (map #(select-keys % keys) xs))

(defn- get-component
  [component-name path]
  (map (fn [xs] {(:name (:attrs xs)) (:content xs)})
    (select-keys-in [:attrs :content]
      (filter #(= (:tag %) component-name)
        (-> path clojure.java.io/input-stream
          xml/parse
          zip/xml-zip
          zip/down
          zip/rights)))))

(defn get-variables-defs
  [path-to-jrxml]
  (get-component :variable path-to-jrxml))

(defn get-fields-defs
  [path-to-jrxml]
  (get-component :field path-to-jrxml))

(get-component :variable  "resources/MasterReport.jrxml")
(get-variables-defs "resources/MasterReport.jrxml")
(get-fields-defs "resources/MasterReport.jrxml")

(defn- zip-tree
  [path]
  (-> path clojure.java.io/input-stream xml/parse zip/xml-zip))

(defn- filter-by-tag
  [tag-name xs]
  (filter #(= (:tag %) tag-name)
          xs))

(defn get-branchs
  "Get main branch from root tree"
  [path branch-name]
  (filter-by-tag branch-name
                 (-> (zip-tree path) zip/children)))

(defn get-names
  [xs]
  (map #(:name (:attrs %)) xs))

(defn find-usages
  [definitions path-to-jrxml]
  (let [names (get-names (get-branchs path-to-jrxml definitions))]
    names))

(map #(:name (:attrs %))
     (map zip/node
       (xz/xml-> (zip-tree "resources/MasterReport.jrxml") :style)))

(map :tag
(zip/children (zip-tree "resources/MasterReport.jrxml")))
(:content (zip/node (xz/xml1-> (zip-tree "resources/MasterReport.jrxml") :title :band :textField :textFieldExpression)))
