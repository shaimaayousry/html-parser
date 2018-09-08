# html-parser-challenge
 HTML Parser Challenge Solution

# Description
 Implement Simple Web page analyzer that uses HTML parser to analysis webpage Information as ( Page Title, HTML Version, Authentication required, headings information and links)
  

# Used Dependencies
 - Spring Boot for managing and configuring web application
 - Jsoup HTML parser used for parsing webpage into HTML document.
 - Maven for Build and Dependency management

# How to build
 Assuming a 'bash' environment and referring to /html-parser-challenge/build.sh
 - execute command 'bash build.sh'
 
# How to run
 Assuming a 'bash' environment referring to /html-parser-challenge/service.sh
 execute command 'bash service.sh'
 - hit web page http://localhost:8088/web_page_analyzer.html
  
 
# Implementation Details
  The implementation is to provide the user with a simple webpage to enter URL of the webpage needs to be analyzed then executes Rest API to get analysis info.
  WePage parsing passes by the following steps:
   - validate given URL (format and connectivity)
   - generate HTML document using JSOUP lib
   - parse page title
   - parse HTML version
   - parse headings
   - parse Authentication Info
   - parse links

# Assumptions
   - HTML page links are assumed to be of 3 types (WEB_URL, EMAIL_LINK and OTHER) only WEB_URL is displayed in the results.
   - Authentication Info is detected (i.e. existence of login form )as long as there exist input field of type password.
   - Simple caching is used to cache the results of analyzing webpages by URL on server side.  
   - "google.com" is Invalid URL format because it is missing protocol.  
   
   - URLs that redirects to redirect to other URLs are detected to INTERNAL URLs (for example: https://l.facebook.com/l.php?u=https%3A%2F%2Finstagram.com%2F&h=ATPTpLlXcvzTFEvDUNw7ZQkTMcr_S0vRLQNT) redirects to instagram.
   
   - Validation of availability of redirect URLs is detected as UNREACHABLE (this mostly occurs because redirected to URLs returning 404 error when missing request parameters or cookies)

  - Validate Links availability is currently depending on configuration "enableValidateReachableURLs" (defined in application.properties) where no validation takes place if this property is set to false
 - If enableValidateReachableURLs is set to false the URL status is displayed as Valid with respect to URL Format validation.
 
 - ParallelStreams are used for parsing page links for providing simple concurrency for demo usage only as it is taken into consideration that parallelStreams are not recommended to use due to its non-determinised computations and the inability to control the number of threads used which will affect the whole system performance.
     
   
# Performance Enhancements
  The main performance issue is in checking the availability of all the links in the webpage, which might be big number of Links, without consuming much time or affect the system performance or affects the other system performance (flood the system of the webpage under analysis with hits which would also be detected as DoS attack) 
  
  In order to manage performance issues, there are some suggestions:
   - Instead of using parallelStreams, use thread pool where the pool capacity will be configured.
   - Process the links in background, where links are added to queue and using configured thread pool, links are processed and the updates are pushed to the web page using web socket protocol.
   
# Design Enhancements
  Applying Pipeline Design pattern on Webpage parsing where each step of the parsing steps (mentioned above in implementation details) can be executed by the responsible Handler and the parsing process would be executing the list of configured handlers, this would help in providing extendable implementation and provide separate of concerns.

# UI Enhancements
  - A checkbox can be added to the webpage "Check Links Availability" where the user can choose whether to analysis the links status or not and to notify the user that this operation might take time.
      
# Caching Enhancements
  - Enhance Cache by applying Eviction Algorithms as LRU (Least Recently Used) or Maximum Life Time

