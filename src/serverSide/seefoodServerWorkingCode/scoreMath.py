import math
"""
Method      : get_score_Percentage
Parameters  : list type ([[, ])
Return      : 
This method calculates the score and returns a percentage
"""
def get_score_Percentage(list):
    foodMatrix = list[0][0]
    foodLable = list[0][1]
    sigmaValue = abs((foodMatrix - foodLable))
    exponentialE = math.exp(sigmaValue * -1)
    percentage = 1 / (1 + exponentialE)
    percentage = percentage * 100
    return float(percentage)


"""
Method      : get_score_PercentageBad
Parameters  : list type ([[, ])
Return      : Float
This method is not used
This method calculates the score and returns a percentage
"""
def get_score_PercentageBad(list):
    foodMatrix = list[0]
    foodLable = list[1]
    percentage = ((abs(foodMatrix - foodLable)) / 4) * 100
    return percentage
