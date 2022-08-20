# -*- coding: utf-8 -*-
import logging
import re
from zhconv import convert

import scrapy


class JavbusSpider(scrapy.Spider):
    name = 'javbus'

    allowed_domains = ['www.javbus.com']
    prefix_url = 'https://www.javbus.com/'
    num_pattern = re.compile(r'\d+')
    jav_num_pattern = re.compile(r'[A-Z]+-\d+')

    def __init__(self, name=None, **kwargs):
        super().__init__(name)
        self.song_num = 0
        logging.basicConfig(level=logging.DEBUG, filename="demo.log", filemode="w",
                            format="%(asctime)s - %(name)s - %(levelname)-9s - %(filename)-8s : " "%(lineno)s line - "
                                   "%(""message)s ", datefmt="%Y-%m-%d %H:%M:%S")

    def start_requests(self):
        f = open('./javNum', encoding='utf-8')
        num_raw = f.read()
        jav_num_list = self.jav_num_pattern.findall(num_raw)
        for num in jav_num_list:
            logging.debug('javNum: ' + num)
            yield scrapy.Request(url=self.prefix_url + num, callback=self.parse)

    def parse(self, response, **kwargs):
        all_info = response.xpath('/html/body/div[5]/div[1]/div[2]').get()

        # for tag in tags:
        #     logging.info(tag)
        yield None
