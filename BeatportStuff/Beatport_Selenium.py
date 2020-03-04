#This project scrapes beatport's new charts and grabs the urls for later optional scraping and adding to spotify playlists
#By: Michael West
import selenium
from selenium import webdriver
from time import sleep
import random
#Create a new chromedriver instance
driver = webdriver.Chrome()
driver.get('https://www.beatport.com/best-new-tracks')
sleep(1)
#If the Beatport "Are you a DJ" box pops up, click no
try:
    djing_box = driver.find_element_by_xpath('//*[@id="bx-element-682647-epINrnK"]/button')
    djing_box.click()
except selenium.common.exceptions.NoSuchElementException:
    try:
        djing_box = driver.find_element_by_xpath('//*[@id="bx-element-682643-epINrnK"]/button')
        djing_box.click()
    except selenium.common.exceptions.NoSuchElementException:
        pass
#Accept the cookies policy if it appears
try:
    cookies = driver.find_element_by_xpath('//*[@id="cookie-banner"]/div/div/button')
    cookies.click()
except  selenium.common.exceptions.NoSuchElementException:
    pass
#Step up to 9 for each chart
urls = []
x = 1
while(x < 10):
    #Scroll if it is a multiple of 3 due to how the beatport site is arranged
    if x % 3 == 0:
        driver.execute_script("window.scrollTo(0, 800)") 
    chart= driver.find_element_by_xpath('//*[@id="slider-marketing-charts-8912"]/ul/li[%d]' % x)
    chart.click()
    #chart.send_keys(selenium.webdriver.common.keys.Keys.RETURN)
    #sleep(random.randint(5, 10))
    driver.back()
    try:
        djing_box = driver.find_element_by_xpath('//*[@id="bx-element-682647-epINrnK"]/button')
        djing_box.click()
        print(selenium.webdriver.Chrome.current_url)
    except selenium.common.exceptions.NoSuchElementException:
        try:
            djing_box = driver.find_element_by_xpath('//*[@id="bx-element-682643-epINrnK"]/button')
            djing_box.click()
            print(selenium.webdriver.Chrome.current_url)
        except selenium.common.exceptions.NoSuchElementException:
            pass
    x += 1
    #sleep(random.randint(5, 10))
print(urls)
