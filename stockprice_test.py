import ast
import json
from datetime import date
from multiprocessing import Pool

import requests


def realtime_stock_info(stock_code_list):
    assert isinstance(stock_code_list, list)
    full_return_dict = {}
    for stock_code in stock_code_list:
        # 股票状态
        wss_response = requests.post(
            'https://aisp.cmft.com/aiisp/api/v1/fin/wind/wss/',
            data=json.dumps({
                # stock_code: 股票代码
                "codes": stock_code,
                # last_trade_day: 最后交易日
                # sec_name : 根据股票代码所得的股票名称（随时变）
                "fields": "last_trade_day,sec_name",
                "sysName": "cmft_slas"
            }),
            headers={'Content-Type': 'application/json'}
        )
        # 股票价格
        wsq_response = requests.post(
            'https://aisp.cmft.com/aiisp/api/v1/fin/wind/wsq/',
            data=json.dumps({
                "codes": stock_code,
                # rt_latest：最新成交价（今天或上一个交易日的）
                # rt_pct_chg：(float) 涨跌幅(今天不是交易日then 收盘价 wrt 开盘价)
                "fields": "rt_latest,rt_pct_chg",
                "sysName": "cmft_slas"
            }),
            headers={'Content-Type': 'application/json'}
        )
        assert wss_response.status_code == 200
        assert wsq_response.status_code == 200
        # print(wss_response.json()['data'])
        wss_value = ast.literal_eval(wss_response.json()['data'])
        wsq_value = ast.literal_eval(wsq_response.json()['data'])
        return_dict = {}
        return_dict['stock_name'] = wss_value[stock_code]['SEC_NAME']
        return_dict['stock_price'] = wsq_value[stock_code]['RT_LATEST']
        today = date.today()
        if wss_value[stock_code]['LAST_TRADE_DAY'] != today.strftime("%Y-%m-%d"):
            return_dict['RT_PCT_CHG'] = None
        else:
            return_dict['price_change'] = wsq_value[stock_code]['RT_PCT_CHG']
        full_return_dict[stock_code] = return_dict
    return full_return_dict

# def realtime_stock_info_sub(stock_code):
#     wss_response = requests.post(
#         'https://aisp.cmft.com/aiisp/api/v1/fin/wind/wss/',
#         data=json.dumps({
#             "codes": stock_code,
#             "fields": "last_trade_day,sec_name",
#             "sysName": "cmft_slas"
#         }),
#         headers={'Content-Type': 'application/json'}
#     )
#     wsq_response = requests.post(
#         'https://aisp.cmft.com/aiisp/api/v1/fin/wind/wsq/',
#         data=json.dumps({
#             "codes": stock_code,
#             "fields": "rt_latest,rt_pct_chg",
#             "sysName": "cmft_slas"
#         }),
#         headers={'Content-Type': 'application/json'}
#     )
#     assert wss_response.status_code == 200
#     assert wsq_response.status_code == 200
#     # print(wss_response.json()['data'])
#     wss_value = ast.literal_eval(wss_response.json()['data'])
#     wsq_value = ast.literal_eval(wsq_response.json()['data'])
#     return_dict = {}
#     return_dict['stock_name'] = wss_value[stock_code]['SEC_NAME']
#     return_dict['stock_price'] = wsq_value[stock_code]['RT_LATEST']
#     today = date.today()
#     if wss_value[stock_code]['LAST_TRADE_DAY'] != today.strftime("%Y-%m-%d"):
#         return_dict['RT_PCT_CHG'] = None
#     else:
#         return_dict['price_change'] = wsq_value[stock_code]['RT_PCT_CHG']
#     return return_dict


print(realtime_stock_info(["600036.SH", '001979.SZ']))
