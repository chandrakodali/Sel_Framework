# Selenium Framework for Automated Testing  

## Overview  
This repository features a comprehensive **Selenium Automation Framework** designed to automate functional and regression testing for web applications. The framework is modular, scalable, and adaptable for real-world testing needs, supporting data-driven testing, cross-browser compatibility, advanced reporting, and integration with CI/CD pipelines.

---

## Key Features  
1. **Data-Driven Testing**:  
   - Developed an Excel Utility to manage test data dynamically, supporting parameterized testing and improving coverage.  

2. **Page Object Model (POM)**:  
   - Enhanced maintainability and modularity by separating UI elements and test logic into reusable components.  

3. **Cross-Browser Testing**:  
   - Supports parallel execution on multiple browsers including Chrome, Firefox, and Internet Explorer.  

4. **Advanced Mouse and Keyboard Automation**:  
   - Integrated Selenium WebDriver with the Robot Class to perform advanced interactions and screen captures.  

5. **Parallel and Multithreaded Execution**:  
   - XML-driven configurations enable concurrent test execution to reduce runtime.  

6. **Reporting with ExtentReports**:  
   - Generates detailed, interactive HTML reports with test results, steps, and failure details.  

7. **CI/CD Integration**:  
   - Configured GitHub Actions for automated test execution and reporting.  

8. **Zephyr Scale Integration**:  
   - Automatically updates test results in Jira, enabling seamless traceability.  

9. **Client-Specific Execution**:  
   - Validated functionality on the client’s VDI environment for compatibility.


---

## Setup and Configuration  

### Prerequisites  
- **Java JDK** 8 or higher installed  
- **Gradle** for build automation  
- **IntelliJ IDEA** or a similar IDE  
- **WebDriverManager** to manage browser drivers  

### Installation  
1. Clone the repository:  
   ```bash  
   git clone https://github.com/Chandu012345/Sel_Framework.git  
   cd Sel_Framework
2. Install dependencies using Gradle:
    gradle build  
3. Update the test data and configuration files under resources/ as per the project requirements.


