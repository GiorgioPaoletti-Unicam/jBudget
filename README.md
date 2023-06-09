# jBudget - Personal Finance Management System

Developed by Giorgio Paoletti, this financial management software offers a comprehensive suite of features for managing personal finances.

## 📃 Overview

The system contains several classes to manage different aspects of finances:

- **LedgerImplementation**: Manages transactions, accounts, scheduled transactions, and categories for transactions.
- **TransactionImplemetation**: Handles a list of movements linked to the transaction date and transaction categories.
- **TagImplementation**: Manages a single category.
- **AccountImplemetation**: Handles a list of movements linked to the account and tracks the account balance.
- **ScheduledTransactionImplementation**: Manages a scheduled transaction through a list of transactions.
- **BudgetImplementation**: Manages a list of categories linked to an expected budget.
- **ClassRegistryImplementation**: Manages instances of all the other classes.
- **BudgetReportImplementation**: Manages the generation of a budget report.
- **ControllerImplementation**: Provides application functionalities.
- **LoggerManagerImplementation**: Logs all actions performed in the ControllerImplementation class.
- **SaverGSon** and **LoaderGSon**: Save and load ledger and budget data respectively to and from JSON files.

## 📁 Features

The system allows you to:

- Add transactions, accounts, and categories.
- Schedule and remove scheduled transactions.
- Remove an account or a transaction.
- Check the validity of a transaction.
- Track the balance of an account and the total amount of a transaction.
- Manage budgets by linking categories to a budget.
- Generate reports relating to the ledger and budget.

## 📋 Testing

The software's correctness and functionality are ensured by various test scenarios developed using the JUnit 5 framework. Test classes include:

- AccountImplementationTest
- LedgerImplementationTest
- MovementImplementationTest
- TransactionImplementationTest
- BudgetImplementationTest
- ScheduledTransactionImplementationTest
- LoaderGSonTest
- SaverGSonTest
- ControllerImplementationTest

Each of these test classes checks different aspects of the associated classes.

## ✉️ Contact

For any questions or clarifications regarding this project, please contact Giorgio Paoletti at [email].

## ⚖️ License

This project is licensed under the MIT License. See the `LICENSE` file for details.
