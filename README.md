# ARX4Dummies
## _An easy way to approch into the data anonymization Java ARX API_



[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

ARX4Dummies is tool written in java who simplify some aspect of the original ARX Library  which include the importing of anonymization data type, the privacy model to use and  statistic results.
Dependencies:
ARX Library 3.9.0 and json-20210307 need to be imported into the project.
## Features
### Importing a CSV Anonymization data structure to specify each field

| COLUMN-NAME | DATA-TYPE | ANONYMIZZATION-TYPE | GERARCHY_PATH |
| ---------- | ------ | ------ | ------ | 
|UID|INTEGER|ID|NULL|-1|-1 |
| Name | STRING | ID | NULL | -1 | -1 |
| Age | INTEGER | QUID | data/hierarchy_age.csv | -1 | -1 |
| Gender | STRING | QUID | data/hierarchy_gender.csv | -1 | -1 | 
| Race | STRING | SENS | data/hierarchy_race.csv | -1 | -1 |
| Date | DATE | QUID | data/hierarchy_date.csv | 0 | 0 |
| City | STRING | UNID | NULL | -1 | -1 | 
| State | STRING | UNID | NULL | -1 | -1 |
| Manner_of_death | STRING | UNID | NULL | -1 | -1 |
| Armed | STRING | UNID | NULL | -1 | -1 |
| Mental_illness | STRING | UNID | NULL | -1 | -1 |
| Flee | STRING | UNID | NULL | -1 | -1 |
Semplificazione del processo di importazione del file csv di input da anonimizzare delle specifiche dei tipi di ogni colonna, tra cui:
- Nome del campo
- Tipo del campo (INTEGER,STRING,DATE)
- Tipologia di anonimizzazione del campo:
    - **(ID) Identifying attributes**  are associated with a high risk of re-identification. They will be removed from the dataset. Typical examples are names or Social Security Numbers.[1]
    - **(QUID) Quasi-identifying attributes** can in combination be used for re-identification attacks. They will be transformed. Typical examples are gender, date of birth and ZIP codes.[1]
    - **(SENS) Sensitive attributes** encode properties with which individuals are not willing to be linked with. As such, they might be of interest to an attacker and, if disclosed, could cause harm to data subjects. They will be kept unmodified but may be subject to further constraints, such as t-closeness or l-diversity. Typical examples are diagnoses.[1]
    - **(UNID) Insensitive attributes** are not associated with privacy risks. They will be kept unmodified.[1]
- Path del file csv contenente la gerarchia di anonizzazione da utilizzare sul campo:
    - gerarchy_path.csv
    - NULL
- Specifica del numero minimo di generalizzazione da utilizzare nella gerarchia del campo:
    - Greater or equal to zero (\>=0) if the number is specified
    - -1 if not specified
- Specifica del numero massimo di generalizzazione da utilizzare nella gerarchia del campo:
    - Greater or equal to zero (\>=0) if the number is specified
    - -1 if not specified
### Modular Privacy Model selection by a setting JSON file

è possibile specificare un json file per l'importazione dei privacy model e la specifica del suppression limit da utilizzare, divisi per **impostazioni generali** (dove i privacy model sono applicati a tutti i tipi di dato quasi-identitifier)  ed **impostazioni degli attributi sensibili** (applicati ai singoli attributi sensibili). Un esempio di impostazione è il seguente:
```json
{ "GeneralSettings":
    { "PrivacyModel" :
        [ ["KAnonymity",2] ],"setSuppressionLimit": 0.5
    },
    {"SensitiveAttrSettings":
        [["DistinctLDiversity","Race",2]]
    }
}
```
Dove **GeneralSettings** supporta i seguenti valori:
- "PrivacyModel" : Lista di configurazioni di privacy models, tra cui:
    - ["KAnonymity",<int>] 
        - E.g.: ["KAnonymity",2]
    - ["KMap",<int>,<float from 0 to 1]> ,"subset"] where subset: "Self" or "USA"
        - E.g.: ["KMap",2,0.1,"Self"] , ["KMap",5,0.5,"USA"]
    - ["AverageReidentificationRisk",<float from 0 to 1>]
        - E.g.: ["AverageReidentificationRisk",0.5]
- "setSuppressionLimit": limite in percentuale delle righe da sopprimere 
    - E.g.: "setSuppressionLimit": 0.5

E dove **SensitiveAttrSettings** supporta i seguenti valori:
- lista di configurazioni di privacy model da applicare agli attributi definiti come sensitive, tra  cui:
    - ["DistinctLDiversity",<Sensitive_attribute_name>,<int>] 
        - E.g.: ["DistinctLDiversity","Race", 2]
    - ["EntropyLDiversity",<Sensitive_attribute_name>,<int>]
        - E.g.: ["EntropyLDiversity","Race", 2]
    - ["RecursiveCLDiversity",<Sensitive_attribute_name>,<int>,<int>] 
        - E.g.: ["RecursiveCLDiversity","Race", 3, 2] 
    - ["EqualDistanceTCloseness",<Sensitive_attribute_name>,<float from 0 to 1>]
        - E.g.: ["EqualDistanceTCloseness","Race",0.2]  

ARX4Dummies richiede che il file JSON delle configurazioni sia tutto nella prima riga del file.

### Simplify the input and output anonymization statistics 

ARX4Dummies semplifica e raggruppa le statistiche più usate ed un subset dei risultati per valutare l'anonimizzazione effettauta da ARX. Esistono due tipologie di statistiche che possono essere effetuate:
- **Statistiche sull'input** che comprendono i seguenti valori:
    - Equivalence classes statistics:
        -   EQAvg
        -   EQNumClasses
        -   EQRecords
        -   suppRecords
	- Risk estimates: Sample-based measures
	   - RAvg
	   - RLowest
	   - RLowestTupleAff
	   - RHighest
	   - RHighestTupleAff
	   - RSampleUniq
       - RPopUniqueZayatz
	- Mixed risks:
	    - ProsecutorRisk
	    - JournalistRisk
	    - MarketerRisk
-  **Statistiche relative all'output** che, oltre a comprendere i valori già citati prima, aggiungono i seguenti valori:
    -  Total Time
    -  Heirarchy optimal generalization
    -  Suppressed records

## Requirements

In order to use ARX4Dummies the following Java JAR libraries have to be imported:
- Libarx-3.9.0.jar
- Json-20210307.jar

## Bibliography
[1] https://arx.deidentifier.org/development/api/