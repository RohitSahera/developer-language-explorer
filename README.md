# Developer Language Explorer

A Java-based data analysis project that processes developer programming-language
data from a CSV file and generates an interactive web dashboard.

This project was built to practice Java through a real data-processing problem
rather than isolated programming exercises.

## What it does

The program takes a CSV file containing developers and the programming
languages they work with.

It then:

- Reads and validates the CSV data
- Creates Java objects representing respondents
- Counts programming-language usage
- Calculates the average number of languages per respondent
- Finds the most common language pairs
- Finds the most common partner for each language
- Sorts the analysis results
- Generates a JSON report
- Displays the results in a browser dashboard

## Project Pipeline

```text
CSV Dataset
     |
     v
  CsvReader
     |
     v
List<Respondent>
     |
     v
LanguageProcessor
     |
     +--> Language popularity
     |
     +--> Language pairs
     |
     +--> Language partnerships
     |
     v
ReportGenerator
     |
     v
report.json
     |
     v
HTML / CSS / JavaScript
     |
     v
Interactive Dashboard