# Vedran Galetić: Mail Online task

## Setup
The project was written in Java 1.8.
Run the project by running the executable jar: _java -jar VedranGaletic_MailOnline.jar_.
Before testing, make sure to input the correct path to the dataset in the "path_to_files.txt" file.

## Task 1. Search engine

The engine supports queries expressed as logical expressions. Currently, the logical operators AND and OR are supported. The operator prioritisation is also taken into account (first resolve ANDs, then ORs).

The names of the subdirectories are taken into account as well (along with manually generated abbreviation terms). Namely, it is assumed that all documents in a folder containing the name component "sci" are relevant for the term "science".

If there are too many search results (i.e., more than 500), a "results.txt" file is generated, containing the list of documents.

The generated search results for "science", "science and religion", and "science or religion" are stored in the corresponding files in the folder "search_results".

## Task 2. tf-idf ranking

If the input is a term without logical operators ("and", "or", surrounded by spaces), then the ranking method is invoked, which yields the documents ranked according to their tf-idf value for the term, along with the value in the parentheses.

## Task 3. Suggestions for improvement

* Include a morphological generator that would enable including "scientific", "scientist", etc. as search terms if the input is "science".
* Treat content words (nouns, verbs, adjectives...) differently from functional words (conjunctions, prepositions...) and pronouns. Possibly treat functional words as modifiers of search procedure.
* Inculde an ontology or Wordnet for hierarchical awareness. E.g., for input "canine or wolf" search only for the superordinate term "canine".
* Give different weight factors to occurrences in text vs. position in a relevant folder. The factors may be modified through search engine performance assessment.
* Employ distributional semantics for matching a document against a term. For example, if we knew the distributional vector of the term and calculate the distributional vector of the doc, we might calculate the cosine similary between the two.
* Employing a distributed data analysis technology would improve the performance of the system. For example, Spark upon commodity hardware, or pure map-reduce jobs upon Hadoop ecosystem (e.g., assign different OR clauses to different nodes).