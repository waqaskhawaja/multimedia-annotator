/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { DataTypeUpdateComponent } from 'app/entities/data-type/data-type-update.component';
import { DataTypeService } from 'app/entities/data-type/data-type.service';
import { DataType } from 'app/shared/model/data-type.model';

describe('Component Tests', () => {
    describe('DataType Management Update Component', () => {
        let comp: DataTypeUpdateComponent;
        let fixture: ComponentFixture<DataTypeUpdateComponent>;
        let service: DataTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [DataTypeUpdateComponent]
            })
                .overrideTemplate(DataTypeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DataTypeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DataTypeService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new DataType(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.dataType = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new DataType();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.dataType = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
