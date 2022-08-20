import scrapy


class JavbusSpider(scrapy.Spider):
    name = 'javbus'
    allowed_domains = ['https://www.javbus.com/']
    start_urls = ['http://https://www.javbus.com//']

    def parse(self, response):
        pass
