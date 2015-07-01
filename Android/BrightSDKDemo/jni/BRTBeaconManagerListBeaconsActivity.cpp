#include "com_bright_examples_demos_BRTBeaconManagerListBeaconsActivity.h"

JNIEXPORT jstring JNICALL Java_com_bright_examples_demos_BRTBeaconManagerListBeaconsActivity_sayHello(JNIEnv *env, jclass){
	return env->NewStringUTF("hello");
}

JNIEXPORT jdoubleArray JNICALL Java_com_bright_examples_demos_BRTBeaconManagerListBeaconsActivity_calculatePosition
  (JNIEnv *env, jclass, jdoubleArray data_in){
    jdouble data_in_cpp[9]; 
    env->GetDoubleArrayRegion(data_in, 0, 9, data_in_cpp);
    jdoubleArray data_out = env->NewDoubleArray(2);
    double *data_out_cpp = new double[2];
    call_triangulation(data_in_cpp[0],data_in_cpp[1],data_in_cpp[2],
                       data_in_cpp[3],data_in_cpp[4], data_in_cpp[5],
                       data_in_cpp[6],data_in_cpp[7],data_in_cpp[8], 
                       data_out_cpp[0], data_out_cpp[1]);
    env->SetDoubleArrayRegion(data_out, 0, 2, data_out_cpp);
    return data_out;
}




jint JNI_OnLoad(JavaVM* vm, void* reserved) {
	JNIEnv* env = NULL;
	jint result = -1;
	if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
		return result;
	}
	return JNI_VERSION_1_4;
}


Point::Point(double x, double y)
{
	this->x = x;
	this->y = y;
};

Point Point::operator +(Point p)
{
	double x = this->x + p.x;
	double y = this->y + p.y;
	Point tmp_p(x, y);
	return tmp_p;
};

double Point::distance_sqr(Point point_b)
{
	return (point_b.x - this->x) * (point_b.x - this->x) + (point_b.y - this->y) * (point_b.y - this->y);
}

double Point::distance(Point point_b)
{
	return (sqrt(distance_sqr(point_b)));
}

Circle::Circle(Point point, double r)
{
	set_circle(point, r);
};



Circle::Circle(double x, double y, double r)
{
	set_circle(x, y, r);
}

void Circle::set_circle(Point point, double r)
{
	this->center = point;
	this->r = r;
};

void Circle::set_circle(double x, double y, double r)
{
	this->center.x = x;
	this->center.y = y;
	this->r = r;
}


int Circle::insect(Circle circle_b, Point intersections[])
/*Function insect
This function is to calculate the crossover points of two circles
	Input:
		circle_b - the antoher circle
	Output:
		intersections - the point list of crossover
	Return:
		the number of crossover points
	Version History
		v0.0 2015-05-31 
			the initial version
Writen by Tony Wang
Copyrights are reserved.
*/
{
	double d, a, b, c, p, q, r;
	double cos_value[2], sin_value[2];
	//Two circles are same
	if (double_equals(this->center.x, circle_b.center.x) && double_equals(this->center.y, circle_b.center.y) && double_equals(this->r, circle_b.r))
		
	{
		return -1;
	}

	//There is no intersection
	d = this->center.distance(circle_b.center);
	if (d > this->r + circle_b.r || d < fabs(this->r - circle_b.r))
		
	{
		intersections[0].x = this->center.x + this->r / (this->r + circle_b.r)*(circle_b.center.x - this->center.x);
		intersections[0].y = this->center.y + this->r / (this->r + circle_b.r)*(circle_b.center.y - this->center.y);
		return 0;
	}
	
	//There is one intersection
	a = 2.0 * this->r * (this->center.x - circle_b.center.x);
	b = 2.0 * this->r * (this->center.y - circle_b.center.y);
	c = circle_b.r * circle_b.r - this->r * this->r - this->center.distance_sqr(circle_b.center);
	p = a * a + b * b;
	q = -2.0 * a * c;
	if (double_equals(d, this->r + circle_b.r) || double_equals(d, fabs(this->r - circle_b.r)))
	{
		cos_value[0] = -q / p / 2.0;
		sin_value[0] = sqrt(1 - cos_value[0] * cos_value[0]);

		intersections[0].x = this->r * cos_value[0] + this->center.x;
		intersections[0].y = this->r * sin_value[0] + this->center.y;

		if (!double_equals(intersections[0].distance_sqr(circle_b.center), circle_b.r * circle_b.r))
		{
			intersections[0].y = this->center.y - this->r * sin_value[0];
		}
		return 1;
	}

	//There are two intersections
	r = c * c - b * b;
	cos_value[0] = (sqrt(q * q - 4.0 * p * r) - q) / p / 2.0;
	cos_value[1] = (-sqrt(q * q - 4.0 * p * r) - q) / p / 2.0;
	sin_value[0] = sqrt(1 - cos_value[0] * cos_value[0]);
	sin_value[1] = sqrt(1 - cos_value[1] * cos_value[1]);

	intersections[0].x = this->r * cos_value[0] + this->center.x;
	intersections[1].x = this->r * cos_value[1] + this->center.x;
	intersections[0].y = this->r * sin_value[0] + this->center.y;
	intersections[1].y = this->r * sin_value[1] + this->center.y;

	if (!double_equals(intersections[0].distance_sqr(circle_b.center), circle_b.r * circle_b.r))
	{
		intersections[0].y = this->center.y - this->r * sin_value[0];
	}
	if (!double_equals(intersections[1].distance_sqr(circle_b.center), circle_b.r * circle_b.r))
	{
		intersections[1].y = this->center.y - this->r * sin_value[1];
	}
	if (double_equals(intersections[0].y, intersections[1].y) && double_equals(intersections[0].x, intersections[1].x))
	{
		if (intersections[0].y >  0) {
			intersections[1].y = 2.0 * this->center.y-intersections[1].y;
		}
		else {
			intersections[0].y = this->center.y - intersections[0].y;
		}
	}
	return 2;

}

Triangulation::Triangulation(Circle *circle_list, int num_circles)
{
	this->circle_list = circle_list;
	this->num_circles = num_circles;
	computation();
}

int Triangulation::computation()
/*Function computation
This function is to compute point in the center of the cross area of three circles
	Input:
		NULL
	Output:
		NULL
	Return:
		an int indicator. 0 - computation failed while 1 - computation successful
	Version History
		v0.0 2015-05-31
			the initial version
Writen by Tony Wang
Copyrights are reserved.
*/
{
	int num_intersections;
	if (num_circles < 3)
	{
		return 0;
	};

	Point point_list[6], temp_point[2];
	Circle temp_circle[2];
	int index = 0;

	for (int i = 0; i < (num_circles - 1); i++)
	{
		temp_circle[0] = this->circle_list[i];
		for (int j = i + 1; j < num_circles; j++)
		{
			temp_circle[1] = circle_list[j];
			num_intersections = temp_circle[0].insect(temp_circle[1], temp_point);
			switch (num_intersections)
			{
			case -1:
				break;
			case 0:
			case 1:
				point_list[index] = temp_point[0];
				index = index + 1;
				break;
			case 2:
				for (int h = 0; h < num_circles; h++)
				{
					if (h != i && h != j)
					{
						if (temp_point[0].distance(circle_list[h].center) < temp_point[1].distance(circle_list[h].center))
						{
							point_list[index] = temp_point[0];
							
						}
						else point_list[index] = temp_point[1];
						index = index + 1;
							
					};
				};
				break;

			};
			

		};
	};
	for (int i = 0; i < index; i++)
	{
		p.x += point_list[i].x;
		p.y += point_list[i].y;
	};
	p.x = p.x / index;
	p.y = p.y / index;
	return 1;
};

int Triangulation::set_circle_list(Circle * circle_list, int num)
{
	if (num < 3)
		return 0;
	else num_circles = 3;
	this->circle_list = circle_list;
	computation();
	return 1;
};

int double_equals(double const a, double const b)
{
	static const double ZERO = 1e-9;
	return fabs(a - b) < ZERO;
}

int call_triangulation(const double x1, const double y1, const double r1, 
	const double x2, const double y2, const double r2, 
	const double x3, const double y3, const double r3, 
	double &x, double &y){
		Circle c_list[3];
		c_list[0].set_circle(x1, y1, r1);
		c_list[1].set_circle(x2, y2, r2);
		c_list[2].set_circle(x3, y3, r3);
		Triangulation t(c_list,3);
		x = t.p.x;
		y = t.p.y;
		return 0;
}
