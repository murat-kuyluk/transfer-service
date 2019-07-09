Feature: Money transfer REST API
  Users/Clients should be able to make a request to transfer money

  Scenario: Money transfers between accounts
    Given at least two accounts have already been provisioned with £999.99 balance
    When users/clients make a request to transfer £150.47 between from/to accounts
    Then from account's balance should have been £849.52
    And to account's balance should have been £1150.46
    And the response should have had transfer id with a status message.

  Scenario: Money transfers between accounts - insufficient fund
    Given at least two accounts have already been provisioned with £999.99 balance
    When users/clients make a request to transfer £1000.99 between from/to accounts
    Then the response should have had insufficient fund message.

  Scenario: Money transfers between accounts - account not exist
    Given at least two accounts have already been provisioned with £999.99 balance
    When users/clients make a request that has invalid account details
    Then the response should have had account not exist message.

  Scenario: Money transfers between accounts - retrieve Transfer
    Given at least two accounts have already been provisioned with £999.99 balance
    And users/clients make a request to transfer £150.47 between from/to accounts
    When users/clients make a request to retrieve the transfer details by its id, 1
    Then the response should have had transfer details

  Scenario: Money transfers between accounts - Transfer not found
    Given at least two accounts have already been provisioned with £999.99 balance
    And users/clients make a request to transfer £150.47 between from/to accounts
    When users/clients make a request to retrieve the transfer details by its id, 999
    Then the response should have had transfer not exist message
