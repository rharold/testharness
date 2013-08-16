Scenario:  JLRIDS-9503 Test1

Given testing jira 9503 I log in with username sdd2spxar and password sdd2spxar
When I use the vin SALVR1BG2CH666836 and start new session
And click the Diagnosis link
And run pptx sequence symtom_selection
And click the DTCs link
And run pptx sequence qualifications2
And click the Save link
Then page should contain text Events by distance in element span

Scenario:  JLRIDS-9503 Test2

Given I return to VSOH page
When click the Recommendations link
And run the 10010 candidate
And run pptx sequence car_config_management
Then page should contain text Events by distance in element span