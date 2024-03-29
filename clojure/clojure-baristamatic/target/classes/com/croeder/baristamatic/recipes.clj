(ns com.croeder.baristamatic.recipes
  (use com.croeder.baristamatic.ingredients)
  )

(defstruct recipe :name
           :coffee :sugar :cream :espresso
           :cocoa :steamed_milk :whipped_cream :foamed_milk
           :price)

(defn recipes []
  { :coffee
   (struct recipe "Coffee",
           3,1,1,0,  0,0,0,0,  2.75),
   :decaf
   (struct recipe  "Decaf Coffee",
           0,1,1,0,  0,0,0,0,  2.75),
   :latte
   (struct recipe  "Caffe Latte",
           0,0,0,2,  0,1,0,0,  2.55),
   :americano
   (struct recipe  "Caffe Americano",
           0,0,0,3,  0,0,0,0,  3.30),
   :mocha
   (struct recipe  "Caffe Mocha",
           0,0,0,1,  1,1,1,0,  3.35),
   :cappuccino
   (struct recipe  "Cappuccino",
           0,0,0,2,  0,1,0,1,  2.90)})

    
(defn display-menu-item
  "returns a string that displays a single menu item as part of a menu"
  [menu-item count] 
             (str count " " (:name menu-item) "    " (:price menu-item) ))

(defn display-menu 
  "given the data-structureds recipes and ingredients,
  this function returns a list of strings that displays
  a numbered menu, with prices, leaving out items for
  which we don't have enough ingredients"
  [recipes-list]
  (let [count 0]
    (map (fn [key]
         (display-menu-item (recipes-list key) (+ count 1)))
       (keys recipes-list))))
